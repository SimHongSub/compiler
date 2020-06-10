package compiler.syntax.syntaxer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import compiler.exception.SyntaxException;
import compiler.lexical.token.Token;

/** 
 * Class that check c source code syntax.
 * 
 * @date 2020.06.09
 * @author SimHongSub
 * @version 1.0
 */
public class Syntaxer {
	/**
	 * parsingTable - SLR parsing table
	 * stack - Data structure to save the current state
	 * syntaxInput - String to consist of token name
	 */
	private Map<String, Map<String, String>> parsingTable;
	private Stack<String> stack;
	private String syntaxInput;
	
	public Syntaxer() {
		parsingTable = new TreeMap<String, Map<String, String>>();
		stack = new Stack<String>();
		syntaxInput = "";
		
		stack.push("0");
		initTable();
	}
	
	/**
	 * Method to syntax analysis for syntaxInput.
	 * 
	 * @param tokens - Tokens created as a result of lexical analysis
	 * @exception SyntaxException.
	 */
	public void analysis(List<Token> tokens) throws SyntaxException {
		
		int fromIndex = 0, blankIndex = 0;	
		String state = stack.peek();
		String key;
		
		generateString(tokens);
		
		while(true) {
			for(int i=fromIndex; i<syntaxInput.length(); i++) {
				if(syntaxInput.charAt(i) == ' ') {
					blankIndex = i;
					break;
				}else if(i == syntaxInput.length() - 1) {
					blankIndex = syntaxInput.length();
					break;
				}
			}
			
			key = syntaxInput.substring(fromIndex, blankIndex);
			
			if (!parsingTable.get(state).containsKey(key)) {
				
				System.out.println("reject!");
				
				throw new SyntaxException(state, key, syntaxInput, fromIndex);
				
				//break;
			}else {
				String nextState = parsingTable.get(state).get(key);
				
				try {
					Integer.parseInt(nextState);
					
					stack.add(nextState);
					fromIndex = blankIndex + 1;
					state = stack.peek();
				}catch (NumberFormatException e) {
					if(nextState.equals("accept")) {
						System.out.println("accept!");
						
						break;
					}else {
						HashMap<String, Integer> map = reduce(state, key, nextState, blankIndex);
						fromIndex = map.get("changeIndex");
						
						for(int i=0;i<map.get("popLength");i++) {
							stack.pop();
						}
						
						state = stack.peek();
					}
				}
			}
		}
		
	}
	
	/**
	 * Method to replace syntax string to fit CFG grammar.
	 * 
	 * @param state - current state
	 * @param key - input source token name
	 * @param cfg - CFG Grammar to reduce
	 * @param index - next input start index(blankIndex)
	 * @return map
	 */
	private HashMap<String, Integer> reduce(String state, String key, String cfg, int index) {
		String[] pieces = cfg.split("->");
		String from = pieces[0], to = pieces[1];
		int popLength = 0, changeIndex = 0;
		String previousString, nextString;
		String currentString = syntaxInput.substring(0, index);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		if(to.equals(" ")) {
			if(state.equals("2") || state.equals("3")) {
				changeIndex = currentString.lastIndexOf(to, index);
				previousString = syntaxInput.substring(0, changeIndex + 1);
				nextString = syntaxInput.substring(changeIndex + to.length() - 1, syntaxInput.length());
				
				syntaxInput = previousString + from + nextString;
				
				changeIndex++;
			}else{
				changeIndex = currentString.lastIndexOf(key, index);
				previousString = syntaxInput.substring(0, changeIndex);
				nextString = syntaxInput.substring(changeIndex - 1, syntaxInput.length());
				
				syntaxInput = previousString + from + nextString;
			}
		}else {
			String[] temp = to.split(" ");
			
			popLength = temp.length;
			changeIndex = currentString.lastIndexOf(to, index);
			previousString = syntaxInput.substring(0, changeIndex);
			nextString = syntaxInput.substring(changeIndex + to.length(), syntaxInput.length());
			
			syntaxInput = previousString + from + nextString;
		}
		
		map.put("changeIndex", changeIndex);
		map.put("popLength", popLength);
		
		return map;
		
	}
	
	/**
	 * Method to generate string to consist of token name.
	 * 
	 * @param tokens - Tokens created as a result of lexical analysis
	 */
	private void generateString(List<Token> tokens) {
		for(int i=0; i<tokens.size(); i++) {
			Token token = tokens.get(i);
			
			if(i == 0) {
				syntaxInput += token.getTokenName();
			}else {
				syntaxInput += " " + token.getTokenName();
			}
		}
		
		syntaxInput += " $";
	}
	
	/**
	 * Method to initialize SLR parsing table.
	 *
	 */
	private void initTable() {	
		String[] state = {"0,vtype,4", "0,$,CODE-> ", "0,CODE,1", "0,VDECL,2", "0,FDECL,3",
				  "1,$,accept",
				  "2,vtype,4", "2,$,CODE-> ", "2,CODE,5", "2,VDECL,2", "2,FDECL,3",
				  "3,vtype,4","3,$,CODE-> ", "3,CODE,6", "3,VDECL,2", "3,FDECL,3",
				  "4,id,7", "4,ASSIGN,8",
				  "5,$,CODE->VDECL CODE",
				  "6,$,CODE->FDECL CODE",
				  "7,assign,11", "7,lparen,10", "7,semi,9",
				  "8,semi,12",
				  "9,for,VDECL->vtype id semi", "9,id,VDECL->vtype id semi", "9,if,VDECL->vtype id semi", "9,rbrace,VDECL->vtype id semi", "9,return,VDECL->vtype id semi", "9,vtype,VDECL->vtype id semi", "9,while,VDECL->vtype id semi", "9,$,VDECL->vtype id semi",
				  "10,rparen,ARG-> ", "10,vtype,14", "10,ARG,13",
				  "11,float,23", "11,id,21", "11,literal,17", "11,lparen,20", "11,num,22", "11,RHS,15", "11,EXPR,16", "11,TERM,18", "11,FACTOR,19",
				  "12,for,VDECL->vtype ASSIGN semi", "12,id,VDECL->vtype ASSIGN semi", "12,if,VDECL->vtype ASSIGN semi", "12,rbrace,VDECL->vtype ASSIGN semi", "12,return,VDECL->vtype ASSIGN semi", "12,vtype,VDECL->vtype ASSIGN semi", "12,while,VDECL->vtype ASSIGN semi", "12,$,VDECL->vtype ASSIGN semi",
				  "13,rparen,24",
				  "14,id,25",
				  "15,rparen,ASSIGN->id assign RHS", "15,semi,ASSIGN->id assign RHS",
				  "16,rparen,RHS->EXPR", "16,semi,RHS->EXPR",
				  "17,rparen,RHS->literal", "17,semi,RHS->literal",
				  "18,addsub,26", "18,rparen,EXPR->TERM", "18,semi,EXPR->TERM",
				  "19,addsub,TERM->FACTOR", "19,multdiv,27", "19,rparen,TERM->FACTOR", "19,semi,TERM->FACTOR",
				  "20,float,23", "20,id,21", "20,lparen,20", "20,num,22", "20,EXPR,28", "20,TERM,18", "20,FACTOR,19",
				  "21,addsub,FACTOR->id", "21,comp,FACTOR->id", "21,multdiv,FACTOR->id", "21,rparen,FACTOR->id", "21,semi,FACTOR->id",
				  "22,addsub,FACTOR->num", "22,comp,FACTOR->num", "22,multdiv,FACTOR->num", "22,rparen,FACTOR->num", "22,semi,FACTOR->num",
				  "23,addsub,FACTOR->float", "23,comp,FACTOR->float", "23,multdiv,FACTOR->float", "23,rparen,FACTOR->float", "23,semi,FACTOR->float",
				  "24,lbrace,29",
				  "25,comma,31", "25,rparen,MOREARGS-> ", "25,MOREARGS,30",
				  "26,float,23", "26,id,21", "26,lparen,20", "26,num,22", "26,EXPR,32", "26,TERM,18", "26,FACTOR,19",
				  "27,float,23", "27,id,21", "27,lparen,20", "27,num,22", "27,TERM,33", "27,FACTOR,19",
				  "28,rparen,34",
				  "29,for,41", "29,id,43", "29,if,39", "29,rbrace,BLOCK-> ", "29,return,BLOCK-> ", "29,vtype,42", "29,while,40", "29,VDECL,37", "29,ASSIGN,38", "29,BLOCK,35", "29,STMT,36",
				  "30,rparen,ARG->vtype id MOREARGS",
				  "31,vtype,44",
				  "32,rparen,EXPR->TERM addsub EXPR", "32,semi,EXPR->TERM addsub EXPR",
				  "33,addsub,TERM->FACTOR multdiv TERM", "33,rparen,TERM->FACTOR multdiv TERM", "33,semi,TERM->FACTOR multdiv TERM",
				  "34,addsub,FACTOR->lparen EXPR rparen", "34,comp,FACTOR->lparen EXPR rparen", "34,multdiv,FACTOR->lparen EXPR rparen", "34,rparen,FACTOR->lparen EXPR rparen", "34,semi,FACTOR->lparen EXPR rparen",
				  "35,return,46", "35,RETURN,45",
				  "36,for,41", "36,id,43", "36,if,39", "36,vtype,42", "36,while,40", "36,rbrace,BLOCK-> ", "36,return,BLOCK-> ", "36,VDECL,37", "36,ASSIGN,38", "36,BLOCK,47", "36,STMT,36",
				  "37,for,STMT->VDECL", "37,id,STMT->VDECL", "37,if,STMT->VDECL", "37,rbrace,STMT->VDECL", "37,return,STMT->VDECL", "37,vtype,STMT->VDECL", "37,while,STMT->VDECL",
				  "38,semi,48",
				  "39,lparen,49",
				  "40,lparen,50",
				  "41,lparen,51",
				  "42,id,52", "42,ASSIGN,8",
				  "43,assign,11",
				  "44,id,53",
				  "45,rbrace,54",
				  "46,float,23", "46,id,21", "46,lparen,20", "46,num,22", "46,FACTOR,55",
				  "47,rbrace,BLOCK->STMT BLOCK", "47,return,BLOCK->STMT BLOCK",
				  "48,for,STMT->ASSIGN semi", "48,id,STMT->ASSIGN semi", "48,if,STMT->ASSIGN semi", "48,rbrace,STMT->ASSIGN semi", "48,return,STMT->ASSIGN semi", "48,vtype,STMT->ASSIGN semi", "48,while,STMT->ASSIGN semi",
				  "49,float,23", "49,id,21", "49,lparen,20", "49,num,22", "49,FACTOR,57", "49,COND,56", 
				  "50,float,23", "50,id,21", "50,lparen,20", "50,num,22", "50,FACTOR,57", "50,COND,58",
				  "51,id,43", "51,ASSIGN,59",
				  "52,assign,11", "52,semi,9",
				  "53,comma,31", "53,rparen,MOREARGS-> ", "53,MOREARGS,60",
				  "54,vtype,FDECL->vtype id lparen ARG rparen lbrace BLOCK RETURN rbrace", "54,$,FDECL->vtype id lparen ARG rparen lbrace BLOCK RETURN rbrace",
				  "55,semi,61",
				  "56,rparen,62",
				  "57,comp,63",
				  "58,rparen,64",
				  "59,semi,65",
				  "60,rparen,MOREARGS->comma vtype id MOREARGS",
				  "61,rbrace,RETURN->return FACTOR semi",
				  "62,lbrace,66",
				  "63,float,23", "63,id,21", "63,lparen,20", "63,num,22", "63,FACTOR,67",
				  "64,lbrace,68",
				  "65,float,23", "65,id,21", "65,lparen,20", "65,num,22", "65,FACTOR,57", "65,COND,69",
				  "66,for,41", "66,id,43", "66,if,39", "66,rbrace,BLOCK-> ", "66,return,BLOCK-> ", "66,vtype,42", "66,while,40", "66,VDECL,37", "66,ASSIGN,38", "66,BLOCK,70", "66,STMT,36",
				  "67,rparen,COND->FACTOR comp FACTOR", "67,semi,COND->FACTOR comp FACTOR",
				  "68,for,41", "68,id,43", "68,if,39", "68,rbrace,BLOCK-> ", "68,return,BLOCK-> ", "68,vtype,42", "68,while,40", "68,VDECL,37", "68,ASSIGN,38", "68,BLOCK,71", "68,STMT,36",
				  "69,semi,72",
				  "70,rbrace,73",
				  "71,rbrace,74",
				  "72,id,43", "72,ASSIGN,75",
				  "73,else,77", "73,for,ELSE-> ", "73,id,ELSE-> ", "73,if,ELSE-> ", "73,rbrace,ELSE-> ", "73,return,ELSE-> ", "73,vtype,ELSE-> ", "73,while,ELSE-> ", "73,ELSE,76",
				  "74,for,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,id,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,if,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,rbrace,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,return,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,vtype,STMT->while lparen COND rparen lbrace BLOCK rbrace", "74,while,STMT->while lparen COND rparen lbrace BLOCK rbrace",
				  "75,rparen,78",
				  "76,for,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,id,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,if,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,rbrace,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,return,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,vtype,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE", "76,while,STMT->if lparen COND rparen lbrace BLOCK rbrace ELSE",
				  "77,lbrace,79",
				  "78,lbrace,80",
				  "79,for,41", "79,id,43", "79,if,39", "79,rbrace,BLOCK-> ", "79,return,BLOCK-> ", "79,vtype,42", "79,while,40", "79,VDECL,37", "79,ASSIGN,38", "79,BLOCK,81", "79,STMT,36",
				  "80,for,41", "80,id,43", "80,if,39", "80,rbrace,BLOCK-> ", "80,return,BLOCK-> ", "80,vtype,42", "80,while,40", "80,VDECL,37", "80,ASSIGN,38", "80,BLOCK,82", "80,STMT,36",
				  "81,rbrace,83",
				  "82,rbrace,84",
				  "83,for,ELSE->else lbrace BLOCK rbrace", "83,id,ELSE->else lbrace BLOCK rbrace", "83,if,ELSE->else lbrace BLOCK rbrace", "83,rbrace,ELSE->else lbrace BLOCK rbrace", "83,return,ELSE->else lbrace BLOCK rbrace", "83,vtype,ELSE->else lbrace BLOCK rbrace", "83,while,ELSE->else lbrace BLOCK rbrace",
				  "84,for,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,id,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,if,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,rbrace,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,return,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,vtype,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "84,while,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace"};
		
		for (String e : state) {
			String[] pieces = e.split(",");
			String from = pieces[0], to = pieces[2];
			if (!parsingTable.containsKey(from))
				parsingTable.put(from, new TreeMap<String, String>());
			
			parsingTable.get(from).put(pieces[1], to);
		}
	}

}
