package compiler.syntax.syntaxer;

import java.util.ArrayList;
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
	
	private Map<String, Map<String, String>> parsingTable;
	private Stack<String> stack;
	private String syntaxInput;
	
	public Syntaxer() {
		parsingTable = new TreeMap<String, Map<String, String>>();
		stack = new Stack<String>();
		syntaxInput = "";
		
		stack.push("1");
		initTable();
	}
	
	public void analysis(List<Token> tokens) throws SyntaxException {
		
		ArrayList<Integer> blankIndexs = new ArrayList<Integer>();
		int fromIndex = 0, idx = 0;
		
		String state = stack.peek();
		
		for(int i=0; i<tokens.size(); i++) {
			Token token = tokens.get(i);
			
			if(i == 0) {
				syntaxInput += token.getTokenName();
			}else {
				syntaxInput += " " + token.getTokenName();
			}
		}
		
		syntaxInput += " $";
		
		/*while(true) {
			int blankIndex = syntaxInput.indexOf(" ", fromIndex);
			
			if(blankIndex != -1) {
				blankIndexs.add(blankIndex);
				
				fromIndex = blankIndex + 1;
			}else {
				break;
			}
		}
		
		blankIndexs.add(syntaxInput.length() + 1);
		
		fromIndex = 0;
		int previousBeginIndex = 0;
		int previousEndIndex = 0;*/
		
		/*for(int i=0; i< syntaxInput.length(); i++) {
			if(syntaxInput.charAt(i) == ' ') {
				String key = syntaxInput.substring(fromIndex, i);
				
				if (!parsingTable.get(state).containsKey(key)) {
					System.out.println("reject!");
					
					break;
				}
				
				String nextState = parsingTable.get(state).get(key);
				
				try {
					Integer.parseInt(nextState);
					
					stack.add(nextState);
					previousBeginIndex = fromIndex;
					previousEndIndex = i;
					fromIndex = i + 1;
					state = stack.peek();
					//idx++;	
				}catch (NumberFormatException e) {
					if(nextState.equals("accept")) {
						System.out.println("accept!");
						
						break;
					}else {
						reduce(nextState, i);
						
						stack.pop();
						fromIndex = previousBeginIndex;
						i = previousEndIndex - 1;
					}
				}
			}
		}*/
		
		while(true) {
			
			int blankIndex = 0;
			
			for(int i=fromIndex; i<syntaxInput.length(); i++) {
				if(syntaxInput.charAt(i) == ' ') {
					blankIndex = i;
					break;
				}else if(i == syntaxInput.length() - 1) {
					blankIndex = syntaxInput.length();
					break;
				}
			}
			
			String key = syntaxInput.substring(fromIndex, blankIndex);
			
			if (!parsingTable.get(state).containsKey(key)) {
				/* String preInput;
				String nexInput;*/
				/* e 처리*/
				/*if((state.equals("4") && key.equals("$")) || (state.equals("5") && key.equals("$"))) {
					preInput = syntaxInput.substring(0, fromIndex);
					
					syntaxInput = preInput + "CODE " + key;
				}else if(state.equals("13") && key.equals("rparen")) {
					preInput = syntaxInput.substring(0, fromIndex);
					nexInput = syntaxInput.substring(fromIndex, syntaxInput.length());
					
					syntaxInput = preInput + "ARG" + nexInput;
				}else if((state.equals("15") && key.equals("$")) || (state.equals("23") && key.equals("$"))) {
					preInput = syntaxInput.substring(0, fromIndex);
					
					syntaxInput = preInput + "MOREARGS " + key;
				}else if(state.equals("19") && key.equals("RETURN")) {
					preInput = syntaxInput.substring(0, fromIndex);
					nexInput = syntaxInput.substring(fromIndex, syntaxInput.length());
					
					syntaxInput = preInput + "BLOCK" + nexInput;
				}else if(state.equals("22") && key.equals("$")) {
					preInput = syntaxInput.substring(0, fromIndex);
					
					syntaxInput = preInput + "BLOCK " + key;
				}*/
				
				System.out.println("reject!");
				
				break;
			}else {
				String nextState = parsingTable.get(state).get(key);
				
				try {
					Integer.parseInt(nextState);
					
					stack.add(nextState);
					fromIndex = blankIndex + 1;
					state = stack.peek();
					//idx++;	
				}catch (NumberFormatException e) {
					if(nextState.equals("accept")) {
						System.out.println("accept!");
						
						break;
					}else {
						HashMap<String, Integer> map = reduce(nextState, blankIndex + 1);
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
	
	public void psrsing(List<Token> tokens) {
		
	}
	
	private HashMap<String, Integer> reduce(String cfg, int index) {
		String[] pieces = cfg.split("->");
		String from = pieces[0], to = pieces[1];
		int popLength;
		int changeIndex = syntaxInput.lastIndexOf(to, index);
		
		if(to.equals(" ")) {
			popLength = 0;
		}else {
			String[] temp = to.split(" ");
			
			popLength = temp.length;	
		}
		
		String previousString = syntaxInput.substring(0, changeIndex);
		String nextString = syntaxInput.substring(changeIndex + to.length(), syntaxInput.length());
		
		syntaxInput = previousString + from + nextString;
		
		System.out.println(syntaxInput);
		
		
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("changeIndex", changeIndex);
		map.put("popLength", popLength);
		
		return map;
		
	}
	
	private void initTable() {
		/*String[] state = {"1,vtype,8", "1,CODE,2", "1,VDECL,4", "1,FDECL,5",
						  "2,$,accept",
						  "3,$,CODE->VDECL CODE",
						  "4,vtype,8", "4,$,CODE->VDECL", "4,CODE,3", "4,VDECL,4", "4,FDECL,5",
						  "5,vtype,8", "5,$,CODE->FDECL", "5,CODE,9", "5,VDECL,4", "5,FDECL,5",
						  "6,vtype,VDECL->vtype ASSIGN semi", "6,$,VDECL->vtype ASSIGN semi",
						  "7,semi,6",
						  "8,id,11", "8,ASSIGN,7",
						  "9,$,CODE->FDECL CODE",
						  "10,vtype,VDECL->vtype id semi", "10,$,VDECL->vtype id semi",
						  "11,assign,46", "11,lparen,13", "11,semi,10",
						  "12,rparen,16",
						  "13,rparen,70", "13,vtype,14", "13,ARG,12",
						  "14,id,15",
						  "15,comma,18", "15,rparen,ARG->vtype id", "15,MOREARGS,17",
						  "16,lbrace,19",
						  "17,rparen,ARG->vtype id MOREARGS",
						  "18,vtype,20",
						  "19,for,26", "19,BLOCK,25", "19,STMT,22", "19,RETURN,22",
						  "20,id,23",
						  "21,rbrace,BLOCK->STMT BLOCK", "21,return,BLOCK->STMT BLOCK",
						  "22,for,26", "22,BLOCK,21", "22,STMT,22",
						  "23,comma,18", "23,MOREARGS,24",
						  "24,rparen,MOREARGS->comma vtype id MOREARGS",
						  "25,return,28", "25,RETURN,27",
						  "26,lparen,29",
						  "27,rbrace,30",
						  "28,float,58", "28,id,56", "28,lparen,40", "28,num,57", "28,FACTOR,31",
						  "29,id,43", "29,ASSIGN,33",
						  "30,vtype,FDECL->vtype id lparen ARG rparen lbrace BLOCK RETURN rbrace", "30,$,FDECL->vtype id lparen ARG rparen lbrace BLOCK RETURN rbrace",
						  "31,semi,32",
						  "32,rbrace,RETURN->return FACTOR semi",
						  "33,semi,34",
						  "34,float,58", "34,id,56", "34,lparen,40", "34,num,57", "34,FACTOR,36", "34,COND,35",
						  "35,semi,38",
						  "36,comp,37",
						  "37,float,58", "37,id,56", "37,lparen,40", "37,num,57", "37,FACTOR,39",
						  "38,id,43", "38,ASSIGN,47",
						  "39,semi,COND->FACTOR comp FACTOR",
						  "40,float,58", "40,id,56", "40,lparen,40", "40,num,57", "40,EXPR,41", "40,TERM,42", "40,FACTOR,49",
						  "41,rparen,44",
						  "42,addsub,45", "42,rparen,EXPR->TERM", "42,semi,EXPR->TERM",
						  "43,assign,46",
						  "44,addsub,FACTOR->lparen EXPR rparen", "44,comp,FACTOR->lparen EXPR rparen", "44,multdiv,FACTOR->lparen EXPR rparen", "44,rparen,FACTOR->lparen EXPR rparen", "44,semi,FACTOR->lparen EXPR rparen",
						  "45,float,58", "45,id,56", "45,lparen,40", "45,num,57", "45,EXPR,48", "45,TERM,42", "45,FACTOR,49",
						  "46,float,58", "46,id,56", "46,literal,52", "46,lparen,40", "46,num,57", "46,RHS,50", "46,EXPR,51", "46,TERM,42", "46,FACTOR,49",
						  "47,rparen,54",
						  "48,rparen,EXPR->TERM addsub EXPR", "48,semi,EXPR->TERM addsub EXPR",
						  "49,addsub,TERM->FACTOR", "49,multdiv,53", "49,rparen,TERM->FACTOR", "49,semi,TERM->FACTOR",
						  "50,rparen,ASSIGN->id assign RHS", "50,semi,ASSIGN->id assign RHS",
						  "51,rparen,RHS->EXPR", "51,semi,RHS->EXPR",
						  "52,rparen,RHS->literal", "52,semi,RHS->literal",
						  "53,float,58", "53,id,56", "53,lparen,40", "53,num,57", "53,TERM,55", "53,FACTOR,49",
						  "54,lbrace,59",
						  "55,addsub,TERM->FACTOR multdiv TERM", "55,rparen,TERM->FACTOR multdiv TERM", "55,semi,TERM->FACTOR multdiv TERM",
						  "56,addsub,FACTOR->id", "56,comp,FACTOR->id", "56,multdiv,FACTOR->id", "56,rparen,FACTOR->id", "56,semi,FACTOR->id",
						  "57,addsub,FACTOR->num", "57,comp,FACTOR->num", "57,multdiv,FACTOR->num", "57,rparen,FACTOR->num", "57,semi,FACTOR->num",
						  "58,addsub,FACTOR->float", "58,comp,FACTOR->float", "58,multdiv,FACTOR->float", "58,rparen,FACTOR->float", "58,semi,FACTOR->float",
						  "59,for,26", "59,BLOCK,60", "59,STMT,22",
						  "60,rbrace,61",
						  "61,for,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "61,rbrace,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "61,retrun,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace",
						  "62,for,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace rbrace",
						  "63,return,28", "63,RETURN,64",
						  "64,rbrace,65",
						  "65,vtype,FDECL->vtype id lparen rparen lbrace BLOCK RETURN rbrace", "65,$,FDECL->vtype id lparen rparen lbrace BLOCK RETURN rbrace",
						  "66,vtype,FDECL->vtype id lparen rparen lbrace RETURN rbrace", "66,$,FDECL->vtype id lparen rparen lbrace RETURN rbrace",
						  "67,rbrace,66",
						  "68,rbrace,69",
						  "69,vtype,FDECL->vtype id lparen ARG rparen lbrace RETURN rbrace", "69,$,FDECL->vtype id lparen ARG rparen lbrace RETURN rbrace",
						  "70,lbrace,71",
						  "71,for,26", "71,return,28", "71,BLOCK,63", "71,RETURN,67"};*/
		
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
				  "11,float,23", "11,id,21", "11,literal,17", "11,lparen,20", "11,num,22", "11,RHS,15", "11,EXPR,26", "11,TERM,18", "11,FACTOR,19",
				  "12,for,VDECL->vtype ASSIGN semi", "12,id,VDECL->vtype ASSIGN semi", "12,if,VDECL->vtype ASSIGN semi", "12,rbrace,VDECL->vtype ASSIGN semi", "12,return,VDECL->vtype ASSIGN semi", "12,vtype,VDECL->vtype ASSIGN semi", "12,while,VDECL->vtype ASSIGN semi", "12,$,VDECL->vtype ASSIGN semi",
				  "13,rparen,24",
				  "14,id,25",
				  "15,rparen,ASSIGN->id assign RHS", "15,semi,ASSIGN->id assign RHS",
				  "16,rparen,RHS->EXPR", "16,semi,RHS->EXPR",
				  "17,rparen,RHS->literal", "17,semi,RHS->literal",
				  "18,rparen,EXPR->TERM", "18,semi,EXPR->TERM",
				  "19,addsub,TERM->FACTOR", "19,rparen,TERM->FACTOR", "19,semi,TERM->FACTOR",
				  "20,float,23", "20,id,21", "20,lparen,20", "20,num,22", "20,EXPR,28", "20,TERM,18", "20,FACTOR,19",
				  "21,addsub,FACTOR->id", "21,comp,FACTOR->id", "21,multdiv,FACTOR->id", "21,rparen,FACTOR->id", "21,semi,FACTOR->id",
				  "22,addsub,FACTOR->num", "22,comp,FACTOR->num", "22,multdiv,FACTOR->num", "22,rparen,FACTOR->num", "22,semi,FACTOR->num",
				  "23,addsub,FACTOR->float", "23,comp,FACTOR->float", "23,multdiv,FACTOR->float", "23,rparen,FACTOR->float", "23,semi,FACTOR->float",
				  "24,lbrace,29",
				  "25,comp,31", "25,rparen,MOREARGS-> ", "25,MOREARGS,30",
				  "26,float,23", "26,id,21", "26,lparen,20", "26,num,22", "26,EXPR,32", "26,TERM,18", "26,FACTOR,19",
				  "27,float,23", "27,id,21", "27,lparen,20", "27,num,22", "27,TERM,33", "27,FACTOR,19",
				  "28,rparen,34",
				  "29,for,41", "29,id,43", "29,if,39", "29,rbrace,BLOCK-> ", "29,return,BLOCK-> ", "29,vtype,42", "29,while,40",
				  "30,rparen,ARG->vtype id MOREARGS", "30,VDECL,37", "30,ASSIGN,38", "30,BLOCK,35", "30,STMT,36",
				  "31,vtype,44",
				  "32,rparen,EXPR->TERM addsub EXPR", "32,semi,EXPR->TERM addsub EXPR",
				  "33,addsub,TERM->FACTOR multdiv TERM", "33,rparen,TERM->FACTOR multdiv TERM", "33,semi,TERM->FACTOR multdiv TERM",
				  "34,addsub,FACTOR->lparen EXPR rparen", "34,comp,FACTOR->lparen EXPR rparen", "34,multdiv,FACTOR->lparen EXPR rparen", "34,rparen,FACTOR->lparen EXPR rparen", "34,semi,FACTOR->lparen EXPR rparen",
				  "35,return,46", "35,RETURN,45",
				  "36,rbrace,BLOCK-> ", "36,return,BLOCK-> ", "36,VDECL,37", "36,ASSIGN,38", "36,BLOCK,47", "36,STMT,36",
				  "37,for,STMT->VDECL", "37,id,STMT->VDECL", "37,if,STMT->VDECL", "37,rbrace,STMT->VDECL", "37,return,STMT->VDECL", "37,vtype,STMT->VDECL", "37,while,STMT->VDECL",
				  "38,semi,48",
				  "39,multdiv,49",
				  "40,multdiv,50",
				  "41,multdiv,51",
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
				  "74,for,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,id,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,if,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,rbrace,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,return,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,vtype,STMT->while lapren COND rparen lbrace BLOCK rbrace", "74,while,STMT->while lapren COND rparen lbrace BLOCK rbrace",
				  "75,rparen,78",
				  "76,for,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,id,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,if,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,rbrace,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,return,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,vtype,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE", "76,while,STMT->if lapren COND rparen lbrace BLOCK rbrace ELSE",
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
