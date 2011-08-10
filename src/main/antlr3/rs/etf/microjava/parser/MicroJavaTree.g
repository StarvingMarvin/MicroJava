tree grammar MicroJavaTree;

options {
	tokenVocab=MicroJava;
	ASTLabelType=CommonTree;
}


@header {
package rs.etf.microjava.parser;

import rs.etf.microjava.syntax.*;
}

@members {
    SymbolTable symtab;
    Scope currentScope;
    public MicroJavaTree(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
        currentScope = symtab.getCurrentScope();
    }
}
// END: header

topdown
    :   enterBlock
    |   enterMethod
    |   enterStruct
    ;

bottomup
    :   exitBlock
    |   exitMethod
    |   exitStruct
    ;

// S C O P E S

enterBlock
    :   BLOCK {currentScope = new LocalScope(currentScope);} // push scope
    ;
exitBlock
    :   BLOCK
        {
        System.out.println("locals: "+currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop scope
        }
    ;

// START: struct
enterStruct // match as we discover struct nodes (on the way down)
    : ^('struct' IDENT .+)
      {
      System.out.println("line "+$IDENT.getLine()+": def struct "+$IDENT.text);
      StructSymbol ss = new StructSymbol($IDENT.text, currentScope);
      currentScope.define(ss); // def struct in current scope
      currentScope = ss;       // set current scope to struct scope
      }
    ;
exitStruct // match as we finish struct nodes (on the way up)
    :   'struct' // don't care about children, just trigger upon struct
        {
        System.out.println("fields: "+currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop scope
        }
    ;
// END: struct

enterMethod
    :   ^(DEFN tsym=type IDENT .*) // match method subtree with 0-or-more args
        {
        System.out.println("line "+$IDENT.getLine()+": def method "+$IDENT.text);
        MethodSymbol ms = new MethodSymbol($IDENT.text, tsym,currentScope);
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        }
    ;
    
exitMethod
    :   DEFN
        {
        System.out.println("args: "+currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop arg scope
        }
    ;


program[SymbolTable symtab] // pass symbol table to start rule

@init {this.symtab = symtab;}
    : ^(PROGRAM const_decl* class_decl* var_decl* .*);



const_decl
    :   ^(DEFCONST tsym=type IDENT literal)
        {
         System.out.println("const: "+$IDENT.text+" type "+tsym.getName());
        VariableSymbol vs = new VariableSymbol($IDENT.text,tsym);
        symtab.define(vs);
        
        }
    ;

literal :	NUMBER | CHAR ;
	

var_decl:	^(DEFVAR tsym=type IDENT) {System.out.println("var: "+$IDENT.text+" type "+tsym.getName());}
		| ^(DEFARR tsym=type IDENT) {System.out.println("array: "+$IDENT.text+" type "+tsym.getName());};

class_decl
	:	^(DEFCLASS IDENT var_decl*) 
	{ 
		System.out.println("def type " + $IDENT.text);
		symtab.define(new StructSymbol($IDENT.text, null));
	};

//method_decl
//	:	('void'|type) IDENT '(' formal_params? ')' var_decl+ '{' (statement)* '}';

type	returns [Type type]
	:	IDENT  {$type = (Type)symtab.resolve($IDENT.text);};

//formal_params
//	:	var_decl (',' var_decl)* -> ^(FORMAL_PARAMS var_decl+);

//statement
//	:	'if' '(' condition ')' statement ('else' statement)?
//	|	'while' '(' condition ')' statement
//	|	'break' ';'
//	|	'return' expr ';'
//	|	'read' '(' designator ')' ';'
//	|	'print' '(' expr (',' NUMBER)? ')' ';'
//	|	designator ('=' expr|'(' actual_params? ')'|'++'|'--')';'
//	|	'{' statement* '}';
	
//actual_params
//	:	expr (',' expr)*;
	
//condition
//	:	condition_term ('||' condition_term)*;

//condition_term
//	:	condition_fact ('&&' condition_fact)*;

//condition_fact
//	:	expr RELOP expr;
	
//expr	:	'-'? term (ADDOP term)*;

//term	:	factor (MULOP factor)*;

//factor	:	designator ('(' actual_params? ')')?
//	|	NUMBER
//	|	CHAR
//	|	'new' type ('['expr']')?
//	|	'(' expr ')';

//designator
//	:	IDENT ('.' IDENT | '[' expr ']')*;
