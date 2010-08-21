function getCSSRule(ruleName, deleteFlag) {						// Return requested style obejct
	ruleName=ruleName.toLowerCase();							// Convert test string to lower case.
	if (document.styleSheets) {                            // If browser can play with stylesheets
		for (var i=0; i<document.styleSheets.length; i++) { // For each stylesheet
			var styleSheet=document.styleSheets[i];          // Get the current Stylesheet
			var ii=0;                                        // Initialize subCounter.
			var cssRule;                               // Initialize cssRule. 
			do {                                           // For each rule in stylesheet
				var s = "";
				if (styleSheet.cssRules) {                    // Browser uses cssRules?
					cssRule = styleSheet.cssRules[ii];         // Yes --Mozilla Style
					if( cssRule != null ) {
						s = cssRule.selectorText;
					} else {
						return false;
					}
				} else {                                      // Browser usses rules?
					cssRule = styleSheet.rules[ii];            // Yes IE style. 
				}                                             // End IE check.
				if (cssRule)  {								// If we found a rule...
					if (s.toLowerCase()==ruleName) { // match ruleName?
						if (deleteFlag=='delete') {             // Yes.  Are we deleting?
							if (styleSheet.cssRules) {           // Yes, deleting...
								styleSheet.deleteRule(ii);        // Delete rule, Moz Style
							} else {                             // Still deleting.
							styleSheet.removeRule(ii);        // Delete rule IE style.
							}                                    // End IE check.
							return true;                         // return true, class deleted.
						} else {                                // found and not deleting.
							return cssRule;                      // return the style object.
						}                                       // End delete Check
					}												// End found rule name
				}                                             // end found cssRule
				ii++;                                         // Increment sub-counter
			} while (cssRule != null);                               // end While loop
		}                                                    // end For loop
	}                                                     // end styleSheet ability check
	return false;                                          // we found NOTHING!
}
/*
function killCSSRule(ruleName) {                          // Delete a CSS rule   
	return getCSSRule(ruleName,'delete');                  // just call getCSSRule w/delete flag.
}*/                                                         // end killCSSRule

function addCSSRule(ruleName, revisionColour) {                           // Create a new css rule
		if (document.styleSheets) {                            // Can browser do styleSheets?
		if (!getCSSRule(ruleName)) {                        // if rule doesn't exist...
			if (document.styleSheets[0].addRule) {           // Browser is IE?
				document.styleSheets[0].addRule(ruleName, "color: " + revisionColour + "; font-style: italic;");      // Yes, add IE style
			} else {                                         // Browser is Moz?
				document.styleSheets[0].insertRule(ruleName+" { color: " + revisionColour + "; font-style: italic; }", 0); // Yes, add Moz style.
			}                                                // End browser check
		}                                                   // End already exist check.
	}                                                    // End browser ability check.
	var cssRule = getCSSRule(ruleName);
	return cssRule;                          // return rule we just created.
}

function createRevisionStyle(revisionId, revisionColour) {
	var cssstyle = addCSSRule(".revision_" + revisionId, revisionColour);
	/*if(cssstyle != null) {
		cssstyle.style.color=revisionColour;
	}*/
	return true;
}
