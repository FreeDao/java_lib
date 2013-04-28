/**
 * AQ policy class manages the policy data and is used for
 * populating the hatching info
 */
AQ.policy =
{
	/**
	 * Returns the policy data (hatching info) for the header image
	 */
	getPolicyHeader: function()
	{
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for images
	 */
	getPolicyImage: function()
	{
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for text
	 */
	getPolicyText: function()
	{
		return AQ.constants.hatchingInfo['none'];
	},



	/**
	 * Returns the policy data (hatching info) for buttons
	 */
	getPolicyButton: function()
	{
		return AQ.constants.hatchingInfo['none'];
	}
}
