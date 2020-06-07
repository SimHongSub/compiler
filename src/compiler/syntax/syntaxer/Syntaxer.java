package compiler.syntax.syntaxer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import compiler.exception.SyntaxException;
import compiler.lexical.token.Token;

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
				System.out.println("reject!");
				
				break;
			}
			
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
					HashMap<String, Integer> map = reduce(nextState, blankIndex);
					fromIndex = map.get("changeIndex");
					
					for(int i=0;i<map.get("popLength");i++) {
						stack.pop();
					}
					
					state = stack.peek();
				}
			}
		}
		
	}
	
	public void psrsing(List<Token> tokens) {
		
	}
	
	private HashMap<String, Integer> reduce(String cfg, int index) {
		String[] pieces = cfg.split("->");
		String from = pieces[0], to = pieces[1];
		
		int changeIndex = syntaxInput.lastIndexOf(to, index);
		
		String[] temp = to.split(" ");
		
		int popLength = temp.length;
		
		String previousString = syntaxInput.substring(0, changeIndex);
		String nextString = syntaxInput.substring(changeIndex + to.length(), syntaxInput.length());
		
		syntaxInput = previousString + from + nextString;
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("changeIndex", changeIndex);
		map.put("popLength", popLength);
		
		return map;
		
	}
	
	private void initTable() {
		String[] state = {"1,vtype,8", "1,CODE,2", "1,VDECL,4", "1,FDECL,5",
						  "2,$,accept",
						  "3,$,CODE->VDECL CODE",
						  "4,vtype,8", "4,CODE,3", "4,VDECL,4", "4,FDECL,5",
						  "5,vtype,8", "5,CODE,9", "5,VDECL,4", "5,FDECL,5",
						  "6,vtype,VDECL->vtype ASSIGN semi", "6,$,VDECL->vtype ASSIGN semi",
						  "7,semi,6",
						  "8,id,11", "8,ASSIGN,7",
						  "9,$,CODE->FDECL CODE",
						  "10,vtype,VDECL->vtype id semi", "10,$,VDECL->vtype id semi",
						  "11,assign,46", "11,lparen,13", "11,semi,10",
						  "12,rparen,16",
						  "13,vtype,14", "13,ARG,12",
						  "14,id,15",
						  "15,comma,18", "15,MOREARGS,17",
						  "16,lbrace,19",
						  "17,rparen,ARG->vtype id MOREARGS",
						  "18,vtype,20",
						  "19,for,26", "19,BLOCK,25", "19,STMT,22",
						  "20,id,23",
						  "21,rparen,BLOCK->STMT BLOCK", "21,semi,BLOCK->STMT BLOCK",
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
						  "46,float,58", "46,id,56", "46,lparen,40", "46,num,57", "46,RHS,50", "46,EXPR,51", "46,TERM,42", "46,FACTOR,49",
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
						  "61,for,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "61,rbrace,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace", "61,retrun,STMT->for lparen ASSIGN semi COND semi ASSIGN rparen lbrace BLOCK rbrace"};
		
		for (String e : state) {
			String[] pieces = e.split(",");
			String from = pieces[0], to = pieces[2];
			if (!parsingTable.containsKey(from))
				parsingTable.put(from, new TreeMap<String, String>());
			
			parsingTable.get(from).put(pieces[1], to);
		}
	}

}
