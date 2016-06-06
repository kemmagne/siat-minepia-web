function chartInc () {
	var data_for_chartt = $('input[id$="pourcentageChartDataa"]').val();
	var text1 = $('input[id$="text"]').val();
	var data_for_chartJsonn = jQuery.parseJSON(data_for_chartt);
	

    $('#container').highcharts({
    	
    	 chart: {
             plotBackgroundColor: null,
             plotBorderWidth: null,
             plotShadow: false,
             type: 'pie'
         },
         title: {
             text:'Historique déclarant par décision'
         },
         tooltip: {
             pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
         },
         plotOptions: {
        	 pie: {
                 allowPointSelect: true,
                 cursor: 'pointer',
                 dataLabels: {
                     enabled: true,
                     format: '<b>{point.name}</b>:{point.y}',
                     style: {
                         color: (Highcharts.theme && Highcharts.theme.contrastTextColor) 
                     },
                     connectorColor: 'silver'
                 }
             }
         },
         series: [{
             name: " ",
             colorByPoint: true,
             data: data_for_chartJsonn,
             point:{
                 events:{
                     click: function (event) {
                   
                    	 document.getElementById('filtredFileItemListForm:type').value = this.name;
                    	 rc()
                     }
       
                 }
             }         
    }]
    });
};