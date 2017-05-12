// Agent security_2 in project SmartBuilding.mas2j



/* Initial beliefs and rules */



/* Initial goals */


!start.
!move.



/* Plans */


+!start : true <- .print("I am working now.").
+!move : true 
	<- next(security_2);
		!move.


