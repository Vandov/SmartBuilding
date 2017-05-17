// Agent paramedic_3 in project SmartBuilding.mas2j



/* Initial beliefs and rules */

injuredFound[source(A)].

/* Initial goals */



//!start.



/* Plans */



//+!start : true <- .print("I am working now.").

+injuredFound[source(A)] <- .print(A, " found an injured. I am busy now!").
