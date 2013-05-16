/**
 * AQ policy class manages the policy data and is used for
 * populating the hatching info
 */
AQ.policy =
{
	isApplyPolicyRules : true,
	aqPolicyRules:{},
	
	getPolicyRules: function(policyTags){
		var dispalyRule = AQ.constants.hatchingInfo['none'];
		if(this.isApplyPolicyRules){
			if(typeof policyTags != "undefined" && policyTags.length>0){
				for(var i in policyTags){
					var tagName = policyTags[i];
					if(this.aqPolicyRules[tagName]){
						if(tagName == "ELEMENT.HIDDEN_WHILE_DRIVING"){
							dispalyRule = (dispalyRule <= AQ.constants.hatchingInfo['hide'])? AQ.constants.hatchingInfo['hide'] : dispalyRule ;
						}else{
							dispalyRule = (dispalyRule <= AQ.constants.hatchingInfo['disable'])? AQ.constants.hatchingInfo['disable'] : dispalyRule ;
						}
					}
				}
			}
		}
		return dispalyRule;
	},
	/**
	 * store the policy rules
	 */
	setPolicyRules: function(policyRules){
		for(var i in policyRules){
			if(typeof this.aqPolicyRules[policyRules[i].action]=="undefined"){
				if(policyRules[i].action.indexOf("/")!=-1 ){
					var rules = policyRules[i].action.split("/");
					for(var i in rules){
						this.aqPolicyRules[rules[i]] =  policyRules[i].allowed;
					}
				}else{
					this.aqPolicyRules[policyRules[i].action] =  policyRules[i].allowed;
				}
			}
		}
	},
		
	/**
	 * Returns the policy data (hatching info) for the header image
	 */
	getPolicyHeader: function()
	{
		// none || disable || hide
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for images
	 */
	getPolicyImage: function()
	{
		// none || disable || hide
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for text
	 */
	getPolicyText: function()
	{
		// none || disable || hide
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for buttons
	 */
	getPolicyButton: function()
	{
		// none || disable || hide
		return AQ.constants.hatchingInfo['none'];
	}
};
