// Agent paramedic_1 in project SmartBuilding.mas2j


/* Initial beliefs and rules */

injuredFound[source(A)].

/* Initial goals */

/* Plans */

+injuredFound[source(A)] : not .desire(start)<- .print(A, " found an injured.");
													!start.

+!start <- startWorking(paramedic_1);
					!start.
