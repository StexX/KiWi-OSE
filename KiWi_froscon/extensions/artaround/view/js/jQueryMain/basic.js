/*
 * 
 * jQuery JavaScript Library v1.3.2
 * 
 *
 */

jQuery(document).ready(function () {
	jQuery('#basic-modal input.basic, #basic-modal a.basic').click(function (e) {
		e.preventDefault();
		jQuery('#basic-modal-content').modal({			
		  onOpen: function (dialog) {					
			dialog.overlay.fadeIn('slow', function () {				
				dialog.data.hide();					
				dialog.container.fadeIn('slow', function () {
					dialog.data.slideDown('slow');
				});					
			});			
		  },
		  onClose: function (dialog) {			  
			dialog.data.fadeOut('slow', function () {
				dialog.container.hide('slow', function () {
					dialog.overlay.slideUp('slow', function () {
						jQuery.modal.close();
					});
				});
			});
		  }		
		});
	});
});