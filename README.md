# Compiler

C language compiler implementation

## Define tokens and regular expression for each token

- Variable type → ( int | char | bool | float )
- Signed integer → ( 0 | (- | e)(1 - 9)(0 - 9)*)
- Literal string → (")( letter | (0 - 9) | blank)*(")
- Boolean string → ( true | false )
- Floating-point number → ( - | Ɛ )( 0 | ( 1 – 9 )( 0 – 9 )*)( . )(0 | ( 0 – 9 )*( 1 – 9 )) - Identifier → ( _ | letter )( _ | letter | ( 0 – 9 ) )*
- Keyword → ( if | else | while | for | return )
- Arithmetic operator → ( + | - | * | / )
- Bitwise operator → ( << | >> | & | | )
- Assignment operator → ( = )
- Comparison operator → ( < | > | == | != | <= | >= )
- Terminating symbol → ( ; )
- Block → ( { | } )
- Paren → ( ( | ) )
- Separating symbol → ( , )
- Whitespaces → ( \n | \t | blank )

## DFA and Transition Table

![Untitled Diagram-Page-5](https://user-images.githubusercontent.com/33109677/102846711-0ebad900-4454-11eb-941c-ade074f2c42f.png)

<img width="700" alt="스크린샷 2020-12-22 오후 12 58 32" src="https://user-images.githubusercontent.com/33109677/102847343-79204900-4455-11eb-8d0b-3cdf5e79110c.png">

## Follow Set

<img width="700" alt="스크린샷 2020-12-22 오후 1 04 43" src="https://user-images.githubusercontent.com/33109677/102847683-49be0c00-4456-11eb-962c-dd30cc347a87.png">

## SLR Parsing Table

#### ACTION Table

<img width="700" alt="스크린샷 2020-12-22 오후 1 00 41" src="https://user-images.githubusercontent.com/33109677/102847563-f3e96400-4455-11eb-8d4b-73ef224523b2.png">

#### GOTO Table

<img width="700" alt="스크린샷 2020-12-22 오후 1 00 58" src="https://user-images.githubusercontent.com/33109677/102847580-fa77db80-4455-11eb-9284-a886b5df4c76.png">

## Implementation

Tech Stack : java

#### compiler.exception package
compiler 의 exception 관련 Class 들을 모아놓은 packcage 이다.  
- LexicalException Class : lexical analyzer 의 exception 을 담당하는 class 이다.  
- SyntaxException Class : syntax analyzer 의 exception 을 담당하는 class 이다.

#### compiler.filesystem package
compiler 의 file system 관련 Class 들을 모아놓은 package 이다. 
- FileProcessing Class : 해당 compiler 의 file I/O 를 담당하는 class 이다.

#### compiler.lexical.lexer package
lexical analyzer 객체인 lexer 와 관련된 Class 들을 모아놓은 package 이다.  
- DFA Class : lexical analyzer 에 사용되는 DFA 의 생성을 담당하는 Class 이다.
- Lexer Class : lexical analyzer 의 객체로, 실제 string tokenize 를 담당하는 Class 이다.

#### compiler.syntax.syntaxer package
syntax analyzer 객체인 syntaxer 와 관련된 Class 들을 모아놓은 package 이다.  
- Syntaxer Class : syntax analyzer 의 객체로, 실제 syntax analysis 를 담당하는 Class 이다.

#### compiler.lexical.token package
lexical analyzer 를 통해 분류되는 token 과 관련된 Class 를 모아놓은 package 이다.
- InputString Class : input 으로 들어오는 C source code string 정보를 담당하는 Class 이다.
- Token Class : lexical analyzer 를 통해 분류된 token 정보를 담당하는 Class 이다.

#### compiler.main package
compiler 의 동작 test 를 위한 Class 들을 모아놓은 package 이다.
- Main Class : compiler 의 수행을 test 해볼 수 있는 test code 가 작성되는 Class 이다.

## Implementation Result

#### Lexer Output

<img width="500" alt="스크린샷 2020-12-22 오후 1 19 32" src="https://user-images.githubusercontent.com/33109677/102848548-69eeca80-4458-11eb-9aeb-853df952a530.png">

#### C language source with no syntax errors

<img width="500" alt="스크린샷 2020-12-22 오후 1 18 44" src="https://user-images.githubusercontent.com/33109677/102848533-61968f80-4458-11eb-96ed-f145b5386277.png">

#### C language source with syntax errors

<img width="500" alt="스크린샷 2020-12-22 오후 1 19 00" src="https://user-images.githubusercontent.com/33109677/102848541-66f3da00-4458-11eb-83f6-ec51a69567ee.png">