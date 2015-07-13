var canvas = $('canvas')[0];
var ctx = canvas.getContext("2d");
document.getElementById("validate").addEventListener('click',function(e){
    jsRoutes.controllers.Application.getPoints().ajax({
        url : '@{Application.getPoints}',
        success : function(res){
            console.log(res)
            for(var i = 0; i < res.length; i++){
                ctx.beginPath();
                ctx.arc(res[i].x+1000, res[i].y+1000, 10, 0, Math.PI*2, true);
                //console.log(res.wheelFL)
                ctx.closePath();
                if(res[i].wheelFL === 10 || res[i].wheelFR === 10 || res[i].wheelRL === 10 || res[i].wheelRR === 10 ){
                    ctx.fillStyle = '#FF0000';
                } else if(res[i].wheelFL === -1){
                    ctx.fillStyle = '#000000';
                } else if(res[i].wheelFL === 18) {

                    ctx.fillStyle = '#006600';
                } else { console.log(res[i].wheelFL)}
                ctx.fill();
            }
            }
    })
})