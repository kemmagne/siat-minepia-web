function disableButton(element)
{
	$(element).prop('disabled', true);
	$(element).addClass("disabledButton");
}
function disableBtn(){
	$(".disBtn").prop('disabled', true);
	$(".disBtn").addClass("disabledButton");
}
function enableBtn(){
	$(".disBtn").prop('disabled', false);
	$(".disBtn").removeClass("disabledButton");
}
function handleSubmit(xhr, status, args, dialog, datalistTable) {
	var jqDialog = jQuery('#' + dialog.id);
	if (args.validationFailed) {
		jqDialog.effect('shake', {
			times : 3
		}, 100);
	} else {
		jQuery(document).ready(function() {
			PF(datalistTable).filter();
		});
		PF(getWidgetVarById(dialog.id).widgetVar).hide();
		setTimeout(function() {
			$('[id$=growl]').hide(1000);
		}, 3000);
	}
}

function getWidgetVarById(id) {
	for ( var propertyName in PrimeFaces.widgets) {
		if (PrimeFaces.widgets[propertyName] != null
				&& PrimeFaces.widgets[propertyName].id === id) {
			return PrimeFaces.widgets[propertyName];
		}
	}
}

function handleSubmitForFlowDblClick(xhr, status, args, dialog) {
	if (args.validationFailed) {
		PF(getWidgetVarById(dialog.id).widgetVar).hide();
	} else {
		PF(getWidgetVarById(dialog.id).widgetVar).show();
	}
}

function handleSubmitWithoutDataTable(xhr, status, args, dialog) {
	var jqDialog = jQuery('#' + dialog.id);
	if (args.validationFailed) {
		jqDialog.effect('shake', {
			times : 3
		}, 100);
	} else {
		PF(getWidgetVarById(dialog.id).widgetVar).hide();
	}
}

// reload attchment panel
function reloadAttachmentPanel(xhr, status, args) {
	window.location.reload();
	
}

function handleSubmitForWorkflow(xhr, status, args, dialog) {
	var jqDialog = jQuery('#' + dialog.id);
	if (args.validationFailed) {
		jqDialog.effect('shake', {
			times : 3
		}, 100);
	} else {
		PF(getWidgetVarById(dialog.id).widgetVar).hide();
	}
}

function handleSubmitForTreeTable(xhr, status, args, dialog) {
	var jqDialog = jQuery('#' + dialog.id);
	if (args.validationFailed) {
		jqDialog.effect('shake', {
			times : 3
		}, 100);
	} else {
		PF(getWidgetVarById(dialog.id).widgetVar).hide();
	}
}
function clickedItem() {
	var formheight = $("#userCreateForm").height();
	var form2height = $("#userEditForm").height();
	var createpage = $("#userListForm").height();
	if (formheight || form2height > 0) {
		$('body').scrollTop(createpage);
	}
}

function tabPosition() {
	$(".accord-content .ui-panel-content> .ui-tabs> .ui-tabs-navscroller> .ui-tabs-nav, .tabnav-head> .ui-tabs-nav").css("marginLeft",  -200).parent().addClass('activDisabled');
}
history.pushState(null, null, '');
window.addEventListener('popstate', function(event) {
    history.pushState(null, null, '');
});
function disableF5(e) { if ((e.which || e.keyCode) == 116 || (e.ctrlKey && e.keyCode == 82)  || (e.which || e.keyCode) == 63240) e.preventDefault(); };

jQuery(document).ready(function() {
	$('.ui-panelmenu-header').prepend('<a href="#" class="sprite"></a>');
	$(".nav .ui-state-active").removeClass("ui-state-active" );
	$('.nav .ui-panelmenu-content').css('display' , 'none')
	$(".headerNotEmpty").parent().addClass("headerNotEmpty-content" );
	$(document).on("keydown", disableF5);
	$("body").on("click", ".gototop", function(e){
		  e.preventDefault();
		  window.scrollTo(0, 0);
	});
	if( window.location.pathname.indexOf("pages/index.xhtml") != -1){
		$('.nav-bar').hide();
	}
	else if( window.location.pathname.indexOf("pages/unsecure/login.xhtml") != -1){
		$('.nav-bar').hide();
	}
	else{
		$('#header').find('.nav-bar').show();
	};
	
	 $('body').on("mouseup",".disBtn", function(){
				$( this ).addClass("disabledButton");
	 } );


	$(".ui-menuitem-link").each(
		function() {
			if (window.location.pathname.indexOf($(this)
					.attr('id').split(":")[1]
					.split("MenuItem")[0]) != -1) {
				$(this).addClass("active");

			}
			else if( window.location.pathname.indexOf($(this).attr('class').split("ui-menuitem-link ui-corner-all ")[1]) != -1){
				$(".ui-menuitem-link").removeClass("active");
				$(this).addClass("active");
		}
				
	});

	var eventresize = 0;
	var resizeme = function(eventresize) {
		var _w = $(window).width(), 
			_h = $(document).height(), 
			$menu = $("#header").height(),
			$navWidth = $(".nav-content .ui-panelmenu-panel").width();
		//$(".nav-content .ui-panelmenu-panel h3").height($navWidth);

		if (_w < 768 && eventresize == 0) {
			$('.nav').removeClass('menuHidden');
			
			$('table').each(function() {
				var $table = $(this);
				var $rows = $table.children('tbody').children();
				$table.children('thead').children().children().each(
					function(i) {
						var text = $(this).html();
						$rows.each(function() {
							$(this).children().eq(i).prepend('<div class="before">'	+ text	+ '</div>')
						})
					})
				});
			eventresize = 1
		} else {
			$('.nav-bar').on(
					'mouseover',
					function() {
						$('.ui-panelmenu-header').closest('.nav-bar')
								.removeClass('menuHidden');
					});
			$('.content-body').on(
					'mouseover',
					function() {
						$('.ui-panelmenu-header').closest('.nav-bar')
								.addClass('menuHidden');
						$(".nav-bar .ui-state-active").removeClass("ui-state-active" );
						$('.nav-bar .ui-panelmenu-content').css('display' , 'none')
					});
			eventresize = 0;
		}
		clickedItem();
	};
	resizeme(eventresize);
	$(window).resize(resizeme(eventresize));

});
