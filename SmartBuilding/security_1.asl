// Agent security in project SmartBuilding.mas2j



/* Initial beliefs and rules */



/* Initial goals */


!start.
!move.
!notify.

/* Plans */

+!start : true <- .print("I am working now.").

+!move : true  
	<- next(security_1);
	lookAround(security_1);
	.send(paramedic_1,tell,injuredFound);
		!move.
					
