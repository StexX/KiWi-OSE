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

Inspiration by some source found in Flot-Plugins

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
function alertObject(obj, maxdepth, showfunction, header)
{   if(!maxdepth) maxdepth = 4 ;
        if(!header) header = '';
        if(!showfunction) showfunction = false;
        var msg = '<script type="text/javascript" src="/Includes/jquery/jquery.js"></script>\n';
                msg += '<script type="text/javascript" src="/Includes/jquery/simpletreeview.js"></script>\n';
                msg += '<h2>' + header + '</h2>\n';
        function openDebug(v)
        {        var w = window.open("Debug.htm");
                w.document.write(v);
                w.document.close();
        }
        function traverse(obj, depth)
        {   if (!depth)
                {        depth = 0;
                        msg += '<ul id="sitemap" class="treeview">\n';
                }
                else {        msg += '<ul>\n'; }
                if(typeof(obj) == "object")
                {         for(var i in obj)
                        {        if (typeof obj[i] == "object")
                                {        msg += '<li><b>' + i + '</b></li>\n';
                                        if (maxdepth > depth) { traverse(obj[i], depth + 1); }
                                }
                                else if(typeof obj[i] == "function")
                                {        if (showfunction) {        msg += '<li>' + i + '&nbsp;' + obj[i].toString() + "</li>\n"; }
                                        else {        msg += '<li>' + i + '&nbsp;<i>' + typeof obj[i] + '</i></li>\n'; }
                                }
                                else {        msg += '<li>' + i + '=' + obj[i] + '&nbsp;<i>' + typeof obj[i] + '</i></li>\n'; }
                        }
                }
                else {        msg += '<li>' + obj + '&nbsp;<i>' + typeof obj[i] + '</i></li>\n'; }
                if (depth == 0)
                {        msg += '</ul>\n <script type="text/javascript">$(document).ready';
                        msg += '(function() {alert("nasowas"); \n $("ul#sitemap").simpletreeview();});</script>';
                }
                else {        msg += '</ul>\n'; }
        }
        traverse(obj);
        openDebug(msg);
}