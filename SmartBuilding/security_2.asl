// Agent security_2 in project SmartBuilding.mas2j



/* Initial beliefs and rules */



/* Initial goals */


!start.
!move.
!notify.

/* Plans */

+!start : true <- .print("I am working now.").
+!move : true 
	<- next(security_2);
	lookAround(security_2);
	.send(paramedic_1,tell,injuredFound);
		!move.
					

