// Agent security_2 in project SmartBuilding.mas2j



/* Initial beliefs and rules */



/* Initial goals */



!move.



/* Plans */


//+!start : true <- .print("I am the security.").
+!move : true 
	<- next(security_2);
		!move.

