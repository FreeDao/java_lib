/**
*AQ.eventqueue
*/

AQ.eventqueue = {
	processingItems : [],
	//complete:true;
	//processTime : 10,
	
	add:function(functionName,functionParam){
		this.processingItems.push({
			"fn":functionName,
			"param":functionParam||[]
		});

		for(var i in this.processingItems){
			console.log(">>>>>>>>>>>>Add Event " + i + " : " + this.processingItems[i].fn);
		}
		
		console.log(">>>>>>>>>>>>sendOk = " + AQ.hash.huState.sendOk);
		
		if(AQ.hash.huState.sendOk){
			this.notifyNext();
		}
	},
	//notifyNext:function(){
		//var that=this;
		
		
		//setTimeout(function(){
			
			//this.process();
		//},that.processTime);
	//},
	
	notifyNext:function(){
		if(this.processingItems.length>0){
			
			var quene = this.processingItems.shift();
			
			AQ.hash.huState.sendOk = false;

			for(var i in this.processingItems){
				console.log(">>>>>>>>>>>>Remain Event " + quene.fn);
			}
			
			switch(quene.fn){
				case "AudioPlay" :
					console.log("audio play ....");
					AQ.respond.switchToAudioMode(quene.param);
					break;
				case "ScreenUpdate":
					///
					console.log("update screen ....");

					for(var i in this.processingItems){
						if(this.processingItems[i].fn == "ScreenUpdate"){
							this.processingItems.splice(i,1);
						}
					}
					
					var screenData = $.extend(true, {}, AQ.hash.stagingPage.data);
					AQ.respond.updateScreen(screenData);
					
					//TODO : remove
					var that = this;
					setTimeout(function(){
						AQ.hash.huState.sendOk = true;
						that.notifyNext();
					},600);
					
					break;
				case "MeterUpdate":
					
					console.log("update meter ....");
					
					AQ.respond.updateMeter(quene.param);
					//Simulate the call back here:
					var that = this; 
					setTimeout(function(){
						AQ.hash.huState.sendOk = true;
						that.notifyNext();
					},200);
					break;
			};
			quene = null;

			return;
			
		}else{
			console.log(">>>>>>>>No more event in Queue...");
			AQ.hash.huState.sendOk = true;
			return;
		}

	}
};