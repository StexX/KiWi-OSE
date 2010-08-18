

KiWiObserver = function() {
	this.events = new Object();
	this.state = new Object();
	
	this.update = function(updatedBy,variableName,newValue) {
		  //alert('observer update ' + variableName);
	      var oldValue = this.state[variableName];
	      this.state[variableName] = newValue;
	      this.lastModifiedBy = updatedBy;
	      // Vyvola udalost zmeny stavoveho objektu, ktera ma jako parametr objekt
	      // s nasledujicimi vlastnostmi:
	      // lastModifiedBy - id objektu, ktery provadel zmenu
	      // changedVariable - nazev menene vlastnosti stavoveho objektu
	      // oldValue - puvodni hodnota menene vlastnosti
	      // newValue - nova hodnota menene vlastnosti

	      var ev = {
	        lastModifiedBy: updatedBy,
	        changedVariable: variableName,
	        oldValue: oldValue,
	        newValue: newValue
	      };

	      this.fireEvent("stateChanged", ev);
	      this.fireEvent("stateChanged__" + variableName, ev);
	};  // update()

	this.fireEvent = function(event, ev) {
		//alert('firing event ' + event);
		var lst = this.events[event];
		if (lst) {
			for (var i = 0; i < lst.length; ++i) {
				//alert('calling handler');
				var func = lst[i];
				func.call(ev);
			}
		}
		else {
			//alert('no handlers');
		}
	}
	
	this.getStateVariable = function(variableName) {
		return this.state[variableName];
	}

	this.addListener = function(eventName, callback) {
		if (this.events[eventName]) {
			this.events[eventName].push (callback);
		}
		else {
			this.events[eventName] = [callback];
		}
	}
	
	this.addVariableListener = function(variableName, handler) {
		this.addListener ("stateChanged__" + variableName, handler);
	}
}

var observer = new KiWiObserver();
window.observer = observer;

