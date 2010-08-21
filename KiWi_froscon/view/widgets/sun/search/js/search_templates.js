var TEMPLATE_SEARCH_WIDGET = '\n\
	<table border="0" width="100%" cellspacing="0" cellpadding="0" class="search initial">\n\
	<tr>\n\
		<td valign="top" colspan="2" class="searchform">\n\
		<a href="index.html"><img src="img/kiwi_logo.png"/></a>\n\
			<form>\n\
				<input type="text" name="Search" size="31">\n\
				<input type="submit" value="Search" name="B1">\n\
			</form>\n\
		</td>\n\
		<td>&nbsp;</td>\n\
	</tr>\n\
	<tr>\n\
		<td valign="top" colspan="2">\n\
			&nbsp;</td>\n\
		<td>&nbsp;</td>\n\
	</tr>\n\
	<tr class="results" style="display:none">\n\
		<td class="sunspace_search_columnleft" valign="top">\n\
			<table border="0">\n\
				<!--\n\
				<tr>\n\
					<td><b>All | Hide Options </b></td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				<tr>\n\
					<td><b>All results</b></td>\n\
				</tr>\n\
				<tr>\n\
					<td><a href="link">WIKI Pages</a></td>\n\
				</tr>\n\
				<tr>\n\
					<td><a href="link">My Share</a></td>\n\
				</tr>\n\
				<tr>\n\
					<td><a href="link">People</a></td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				-->\n\
				<tr>\n\
					<td class="orderBy">\n\
						<span><b>Order by</b></span><br/>\n\
						<a title="modified">Latest</a><br/>\n\
						<a title="ceq" class="selected">Top CEQ</a><br/>\n\
					</td>\n\
				</tr>\n\
			</table>\n\
			&nbsp;\n\
		</td>\n\
		<td class="sunspace_search_columnmiddle" valign="top">\n\
			<div class="loadingContainer" style="display:none">\n\
				<img src="../common/img/wait.gif">Please wait ...\n\
			</div>\n\
			<div class="noResultsContainer" style="display:none">\n\
				Your query didn\'t return any results.\n\
			</div>\n\
			<table border="0" width="100%" cellspacing="0" cellpadding="0">\n\
			</table>\n\
			<div class="pagination"></div>\n\
		</td>\n\
		<td class="sunspace_search_columnright">\n\
			<table border="0" width="100%">\n\
				<tr>\n\
					<td><span><b>Filter</b></span></td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				<tr>\n\
					<td>\n\
						<span><b>Communities</b></span><br/>\n\
						<div class="communities">\n\
						</div>\n\
					</td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				<tr>\n\
					<td>\n\
						<span><b>People</b></span><br/>\n\
						<div class="people">\n\
						</div>\n\
					</td>\n\
				</tr>\n\
				<tr>\n\
					<td>&nbsp;</td>\n\
				</tr>\n\
				<tr>\n\
					<td class="sunspace_search_tagcloud">\n\
						<span><b>Tag Cloud</b><span><br/>\n\
						<div class="tags">\n\
						</div>\n\
					</td>\n\
					\n\
				</tr>\n\
			</td>\n\
				</tr>\n\
			</table>\n\
		</td>\n\
	</tr>\n\
</table>';

var TEMPLATE_SEARCH_Separator = '<td class="sunspace_search_divider" colspan="3"><div class="dividerLine"></div></td>';

var TEMPLATE_SEARCH_ContentItem = '\n\
				<td class="sunspace_search_tdleft">&nbsp;</td>\n\
					<td class="sunspace_search_tdmiddle">\n\
						<a href="${view_url}">${title}</a>\n\
						<br>\n\
						${description}\n\
						<br>\n\
						<a href="${view_author_url}">${author_name}</a>\n\
					</td>\n\
					<td class="sunspace_search_tdright">\n\
					<table border="0" width="100%" cellspacing="0" cellpadding="0">\n\
						<tr>\n\
							<td class="sunspace_search_equity">\n\
								<div class="rating" style="background-position:${rating_xpos} 0px"></div>\n\
							</td>\n\
							<td>\n\
								&nbsp;\n\
								<!--<img border="0" src="img/sunspace_search_info.png">-->\n\
							</td>\n\
						</tr>\n\
						<tr>\n\
							<td class="sunspace_search_equity">Information Equity:</td>\n\
							<td>${iq}</td>\n\
						</tr>\n\
						<tr>\n\
							<td class="sunspace_search_equity">Hits:</td>\n\
							<td>${views}</td>\n\
						</tr>\n\
					</table>\n\
				</td>';

var TEMPLATE_SEARCH_PersonItem = '\n\
				<td class="sunspace_search_tdleft">\n\
					<img border="0" src="img/user.jpg" width="48" height="48">\n\
				</td>\n\
				<td class="sunspace_search_tdmiddle">\n\
					<a href="view_url">${first_name} ${last_name}</a>, ${phone}<br>\n\
					${title}<br>\n\
					<a href="mailto:${email}">${email}</a>\n\
				</td>\n\
				<td class="sunspace_search_tdright">\n\
				<table border="0" width="100%" cellspacing="0" cellpadding="0">\n\
					<tr>\n\
						<td class="sunspace_search_equity">&nbsp;</td>\n\
						<td>&nbsp;</td>\n\
					</tr>\n\
					<tr>\n\
						<td class="sunspace_search_equity">Contribution Equity:</td>\n\
						<td>${ceq}</td>\n\
					</tr>\n\
					<tr>\n\
						<td class="sunspace_search_equity">Participation Equity:</td>\n\
						<td>${peq}</td>\n\
					</tr>\n\
				</table>\n\
				</td>';

var TEMPLATE_SEARCH_CommunityItem = '\n\
				<td class="sunspace_search_tdleft">\n\
					<div class="sunspace_search_img"></div>\n\
				</td>\n\
				<td class="sunspace_search_tdmiddle">\n\
					<a href="${view_url}">${name}</a>\n\
					<br>\n\
					Members: ${member_count}<br>\n\
				</td>\n\
				<td class="sunspace_search_tdright">\n\
					<table border="0" width="100%" cellspacing="0" cellpadding="0">\n\
						<tr>\n\
							<td class="sunspace_search_equity">Top Contributor:</td>\n\
							<td><a href="${top_contributor_url}">${top_contributor_name}</a></td>\n\
						</tr>\n\
						<tr>\n\
							<td class="sunspace_search_equity">Community Equity:</td>\n\
							<td>${ceq}</td>\n\
						</tr>\n\
						<tr>\n\
							<td class="sunspace_search_equity">Contributions</td>\n\
							<td>${contribs_count}</td>\n\
						</tr>\n\
					</table>\n\
				</td>';



(function( $ ) {

  $.Search.computeViewURL = function(url) {
	  return "http://kiwi.sunsolutioncenter.de/KiWi/home.seam?kiwiid=uri::" + encodeURIComponent(url);
  }

  $.Search.ContentItem = {

	  template : $.template(TEMPLATE_SEARCH_ContentItem),

	  deploy : function(rowEl, item){
		  // Round the IQ
		  item.iq = Math.round(item.iq);

		  // Compute xPos background position for rating style
		  // The rating is shown using a single background picture
		  // with 5 red stars followed by 5 gray stars; with positiniong
		  // and clipping, we can show any of the 5 "star rating"
		  item.rating_xpos = Math.round((5 - item.rating) * -14) + "px";

		  // Compute view URLs
		  item.view_url = $.Search.computeViewURL(item.url);
		  item.view_author_url = $.Search.computeViewURL(item.author_url);

		  // And render the template
		  $(rowEl).append(this.template, item);
	}
  }

  $.Search.PersonItem = {

	  template : $.template(TEMPLATE_SEARCH_PersonItem),

	  deploy : function(rowEl, item){
		  // Compute view URLs
		  item.view_url = $.Search.computeViewURL(item.url);

		  // And redner the template
		  $(rowEl).append(this.template, item);
	}
  }
  
  $.Search.CommunityItem = {

		template : $.template(TEMPLATE_SEARCH_CommunityItem),

		deploy : function(rowEl, item){
			// Compute view URLs
			item.view_url = $.Search.computeViewURL(item.url);

			// Create some "compound values" first; templates cannot
			// traverse objects
			item.top_contributor_name = item.top_contributor.name;
			item.top_contributor_url = item.top_contributor.url;

			// And render
			$(rowEl).append(this.template, item);
	}
  }


})(jQuery);