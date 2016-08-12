//var PlayUNO;
//$(function(){
//   $("#btnCreate").on("click",function(){
//         $.getJSON("newGame",function(){
//        }).done(function(result){
//            PlayUNO.gameID = result.UNOID;
//             console.log(PlayUNO);
//            $("#TableDiv").empty().append($("<center><h2>").text("Game ID = " + PlayUNO.gameID))
//                    .append($("<center><h1>").text("Waiting for another players...."));
//
//   });
//});
//});

//$(function() {
//
//	var gameTemplate = Handlebars.compile($("#gameTemplate").html());
//	var membersTemplate = Handlebars.compile($("#membersTemplate").html());
//
//	$("#btnCreate").on("singletap", function() {
//		var promise = $.getJSON("api/team");
//		promise.done(function(result) {
//			$("#all-teams")
//					.append(teamsTemplate({teams: result}));
//		});
//	});
