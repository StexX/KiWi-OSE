/*
 * The MIT License

Copyright (c) 2010 by Juergen Marsch

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
/*
Flot plugin for Bubbles data sets

  series: {
    Bubbles: null or true
  }
data: [

  $.plot($("#placeholder"), [{ data: [ ... ], Bubble: true }])

*/

(function ($) {
    var options = {
        series: {
                        bubbles: {active: false
                                , show: false
                                , fill: true
                                , lineWidth: 2
                                , highlight: { opacity: 0.5 }
                                , drawbubble: drawbubbleDefault
                        }
                }
    };
    var  data = null, canvas = null, target = null, axes = null, offset = null, highlights = [];
        function drawbubbleDefault(ctx,series, x,y,r,c,overlay)
        {        ctx.fillStyle = c;
                ctx.strokeStyle = c;
                ctx.lineWidth = series.bubbles.lineWidth;
                ctx.beginPath()
                ctx.arc(x,y,r,0,Math.PI*2,true);
                ctx.closePath();
                if (series.bubbles.fill) {        ctx.fill();}
                else {        ctx.stroke(); }
        }
    function init(plot) {
              plot.hooks.processOptions.push(processOptions);
        function processOptions(plot,options)
        {         if (options.series.bubbles.active)
            {   plot.hooks.draw.push(draw);
                plot.hooks.bindEvents.push(bindEvents);
                plot.hooks.drawOverlay.push(drawOverlay);
            }
        }
                function draw(plot, ctx)
                {        var series;
            canvas = plot.getCanvas();
            target = $(canvas).parent();
            axes = plot.getAxes();
            offset = plot.getPlotOffset();
            data = plot.getData();
                        for (var i = 0; i < data.length; i++)
                        {        series = data[i];
                                if (series.bubbles.show) {
                                        for (var j = 0; j < series.data.length; j++) {
                                                drawbubble(ctx,series, series.data[j], series.color);
                                        }
                                }
                        }
                }
                function drawbubble(ctx,series,data,c,overlay)
                {        var x,y,r;
                        x = offset.left + axes.xaxis.p2c(data[0]);
                        y = offset.top + axes.yaxis.p2c(data[1]);
                        r = parseInt(axes.yaxis.scale * data[2] / 2);
                        series.bubbles.drawbubble(ctx,series,x,y,r,c,overlay);
                }

                function bindEvents(plot, eventHolder)
        {         var r = null;
            var options = plot.getOptions();
            var hl = new HighLighting(plot, eventHolder, findNearby, options.series.bubbles.active,highlights)
        }
        function findNearby(plot,mousex, mousey){
                        var series, r;
                        axes = plot.getAxes();
                        for (var i = 0; i < data.length; i++) {
                                series = data[i];
                                if (series.bubbles.show) {
                                        for (var j = 0; j < series.data.length; j++) {
                                                var dataitem = series.data[j];
                                                var dx = Math.abs(axes.xaxis.p2c(dataitem[0]) - mousex)
                                                  , dy = Math.abs(axes.yaxis.p2c(dataitem[1]) - mousey)
                                                  , dist = Math.sqrt(dx * dx + dy * dy);
                                                if (dist <= dataitem[2]) {r = {i: i,j: j}; }
                                        }
                                }
                        }
                        return r;
                }
                function drawOverlay(plot, octx)
                {       
                        //TODO maybe return because not correct
                        //octx.clearRect(0, 0, target.width, target.height);
                         octx.clearRect(0, 0, 100, 100);
                        for (i = 0; i < highlights.length; ++i) {
                                drawHighlight(highlights[i]);
                        }
                        octx.restore();
                        function drawHighlight(s){
                                //var c = "rgba(255, 255, 255, " + s.series.bubbles.highlight.opacity + ")";
                                //drawbubble(octx, s.series, s.point, c, true);
                        }
                }
    }

    $.plot.plugins.push({
        init: init,
        options: options,
        name: 'bubbles',
        version: '0.2'
    });
})(jQuery);