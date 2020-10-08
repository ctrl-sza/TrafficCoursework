2nd Year group project (2018)

Had to update legacy code for an existing automatic congestion charging system with the aim of making the system easier for drivers to use. I worked in a 3 person team.   

Coursework specifications:

"   a) add unit tests for the existing code
    b) refactor the existing code in order to achieve the above 
    c) write new code and tests for the new functionality
    d) refactor your new code to improve its design 

Currently, vehicle number plates are scanned by cameras at the edge of city-centre zone and as the vehicle crosses the boundary, the event is logged in the congestion charge system. A process then runs once a day working out how long each vehicle spent in the zone, and charging drivers’ accounts accordingly. Vehicle owners are charged per minute that they are inside the zone.
Drivers create an account and top it up from their credit card online through another system - that payment system is looked after by an external supplier. If drivers do not have enough credit on their account when the charge is made, then they face a penalty.
Wessex have decided to make a change so that the charges are not so sensitive to exactly how long vehicles spend inside the zone, as they suspect this has led to people driving faster and also makes charges less predictable. The new rule is that if you enter the zone before 2pm, you are charged £6, and you can stay in the zone for up to 4 hours. If you enter the zone after 2pm, you will be charged £4. If you leave and come back within 4 hours, you should not be charged twice. If you stay inside the zone for longer than 4 hours on a given day then you will be charged £12."