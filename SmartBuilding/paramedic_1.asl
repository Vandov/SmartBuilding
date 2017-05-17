// Agent paramedic_1 in project SmartBuilding.mas2j



/* Initial beliefs and rules */

//at(P) :- pos(P,X,Y) & pos(paramedic_1,X,Y).

injuredFound[source(A)].

/* Initial goals */

//!start.
/* Plans */

//+!start : true <- .print("I am working now.").

//+injuredFound[source(A)] : true <- .print(A, " found an injured. I am on my way!").

/*+!at(L) : at(L).
+!at(L) <- ?pos(L,X,Y);
           move_towards(X,Y);
		   .print("On my way.");
           !at(L).*/
		   
+injuredFound[source(A)] <- .print(A, " found an injured. I am busy now!").
