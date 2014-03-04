<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="./includes.jsp"%>

<c:forEach var="entry" items="${articleMap}" varStatus="loop">

	<c:choose>
 		<c:when test="${rowColor == 'one'}"><c:set var="rowColor" value="two"/></c:when>
 		<c:otherwise><c:set var="rowColor" value="one"/></c:otherwise>
	</c:choose>
	
	<div class="row ${rowColor}">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div id="story-${entry.key}" class="story row">

			<c:forEach var="row" items="${entry.value}" varStatus="cnt">
				<div class="item">
					<div class="item_title col-xs-12">
						<a href="${row.link}" target="_blank">${row.title}</a>
					</div>
					
					<c:if test="${not empty row.img}">
					<div> <img class="item_img" src="${row.img}"/></div>
					</c:if>
					<c:if test="${empty row.img}">
					<hr class="item_title-separator">
					</c:if>
					
					<div class="item_content">${row.mainContents}</div>
					
					<div class="panel-group panel-group-sArt">
					  <div class="panel panel-default panel-sArt" >
					   
					    <div class="panel-heading panel-heading-sArt row">
					    
					      <h4 class="panel-title panel-${entry.key} col-xs-4">
					        <a data-toggle="collapse" data-parent="#accordion" href="#collapse-trans-${entry.key}-${cnt.index}">
					          <span class="glyphicon glyphicon-transfer"></span>
					        </a>
					      </h4>
					      
					      <h4 class="panel-title col-xs-4">
					      	<a class="js-add-article" href="#">
					          <span class="glyphicon glyphicon-plus icon-disabled"></span>
					        </a>
					      </h4>
					      
					      <h4 class="panel-title panel-${entry.key} col-xs-4">
					        <a data-toggle="collapse" data-parent="#accordion" href="#collapse-${entry.key}-${cnt.index}">
					         <span class="glyphicon glyphicon-list <c:if test="${empty row.simularList}"> icon-disabled </c:if>"></span>
					        </a>
					      </h4>
					      
					    </div>
					    
					   
					    <div id="collapse-${entry.key}-${cnt.index}" class="collapse-${entry.key} panel-collapse panel-collapse-sArt collapse">
					      <div class="panel-body panel-body-sArt">
					      	<c:forEach var="sArticle" items="${row.simularList}">
						      	<div class="panel-article-info col-xs-12">
									<a href="${sArticle.link}" target="_blank">${sArticle.title}</a>
								</div>
					      	</c:forEach>
					      </div>
					    </div>
					     
					    
					    <div id="collapse-trans-${entry.key}-${cnt.index}" class="collapse-${entry.key} panel-collapse panel-collapse-sArt collapse">
					      <div class="panel-body panel-body-sArt">
					     	 <div class="panel-article-info col-xs-12">
						      	${row.transedContents}
						     </div>
					      </div>
					    </div>
					    
					  </div>
  					</div>
					
				</div>
				
			</c:forEach>
				
			</div>
		</div>
		<!-- // article section -->
	</div>
	
    <script type="text/javascript">
	    $('#story-${entry.key}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});
	    
	    $('.collapse-${entry.key}').on('shown.bs.collapse hidden.bs.collapse', function () {
	    	$('#story-${entry.key}').each(function() {
				var $container = $(this);
				$container.imagesLoaded(function() {
					$container.packery({
						itemSelector : '.item',
						gutter : 5
					});
				});
			});
	    });
	   
    </script>
</c:forEach>