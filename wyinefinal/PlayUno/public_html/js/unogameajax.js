$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://10.10.24.241:8080/UNOWebServer/api/game/gamelist', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {

			    	alert( "success" );
			        var json = $.parseJSON(data);

			        for (var i=0; i<json.length; i++)
			        {
			            $('#demo').append('<div class="name">'+ json[i].gid  + " : " + json[i].name +'</div>');
			        }
			    }

			});
		});