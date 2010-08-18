var TEMPLATE_SN_SEARCH_WIDGET = '\n\
<div id="search"><form>\n\
    <!-- right side -->\n\
    <div id="right">\n\
        <div id="selected_facets"></div>\n\
        <div id="facets"></div>\n\
    </div>\n\
    <!-- middle -->\n\
    <div id="center">\n\
    <div id="search_bar">\n\
            <table>\n\
                <tr>\n\
                    <td id="search_field_td"><input type="text" id="search_field_input" /></td>\n\
                    <td id="search_button_td"><input type="submit" id="search_button_input" value="Suche"/>\n\
                    </td>\n\
                </tr>\n\
            </table>\n\
        <ul id="search_menu">\n\
            <li><a href="http://www.123people.at/s/test" target="_blank">Personensuche im Internet |</a></li>\n\
            <li><a href="http://tagit2.salzburgresearch.at" target="_blank">Kartensuche</a></li>\n\
        </ul>\n\
    </div>\n\
	<div id="search_paging_top"></div>\n\
    <ul id="type_tabs"></ul>\n\
    <div id="search_results">\n\
    <div id="placeholder1" class="placeholder" >\n\
        <img src="img/placeholder.jpg"/>\n\
    </div>\n\
    <div id="single_results"></div>\n\
    <div id="placeholder2" class="placeholder">\n\
        <img src="img/placeholder.jpg"/>\n\
    </div>\n\
    <div id="search_paging">\n\
    </div>\n\
    </div>\n\
    </div>\n\
    <div style="clear:both;"></div>\n\
    <div id="loading" style="display:none"></div>\n\
    </form></div>\n\
';

var TEMPLATE_SEARCH_ArticleItem = '\n\
                        <div class="single_result" style="border: 1px dashed navy;">\n\
                            <h2><img style="width: 20px; margin-right: 10px; margin-bottom: -2px;" src="img/sn.png"><a style="color:navy;" onclick="linkTo(\'${id}\')">${title}</a></h2>\n\
                            <div class="description">\n\
                                ${description}\n\
                                <a onclick="linkTo(\'${id}\')"> [mehr]</a>\n\
                            </div>\n\
                            <p class="info">\n\
                                Aussen / ${modified} / <span class="red">Online</span>\n\
                            </p>\n\
                        </div>';

var TEMPLATE_SEARCH_ImageItem = '\n\
                        <div class="single_result">\n\
                            <h2><a onclick="linkTo(\'${id}\')">${title}</a></h2>\n\
							<div style="width:100px;height:100px;background-color:lightgrey;"></div>\n\
                        </div>';

var TEMPLATE_SEARCH_BlogItem = '\n\
                         <div class="single_result" style="border: 1px dashed green;">\n\
                            <h2><img style="width: 20px; margin-right: 10px; margin-bottom: -2px;" src="img/blog.png"><a style="color:green;"onclick="linkTo(\'${id}\')">${title}</a></h2>\n\
                            <div class="description">\n\
                                ${description}\n\
                                <a onclick="linkTo(\'${id}\')"> [mehr]</a>\n\
                            </div>\n\
                            <p class="info">\n\
                                Aussen / ${modified} / <span class="red">Online</span>\n\
                            </p>\n\
                        </div>';
                        
var TEMPLATE_SEARCH_DefaultItem = '\n\
                        <div class="single_result">\n\
                            <h2>Default: <a onclick="alert(\'do semething default\')">${title}</a></h2>\n\
                        </div>';



(function( $ ) {


  $.Search.ArticleItem = {

	  template : $.template(TEMPLATE_SEARCH_ArticleItem),

	  deploy : function(div, item){

		  // And render the template
		  $(div).append(this.template, item);
	}
  }

  $.Search.ImageItem = {

	  template : $.template(TEMPLATE_SEARCH_ImageItem),

	  deploy : function(div, item){
		  $(div).append(this.template, item);
	}
  }
  
  $.Search.BlogItem = {

		template : $.template(TEMPLATE_SEARCH_BlogItem),

		deploy : function(div, item){
			$(div).append(this.template, item);
	}
  }

  $.Search.DefaultItem = {

		template : $.template(TEMPLATE_SEARCH_DefaultItem),

		deploy : function(div, item){
			$(div).append(this.template, item);
	}
  }


})(jQuery);