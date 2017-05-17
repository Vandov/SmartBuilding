// Agent paramedic_2 in project SmartBuilding.mas2j



/* Initial beliefs and rules */

injuredFound[source(A)].

/* Initial goals */


/* Plans */

+injuredFound[source(A)] <- .print(A, " found an injured. I am busy now!").
