<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="./includes.jsp"%>

<c:if test="${empty page.content}">end</c:if>

<c:if test="${not empty page.content}">

	<c:choose>
		<c:when test="${page.number % 2 == 0}">
			<c:set var="rowColor" value="two" />
		</c:when>
		<c:otherwise>
			<c:set var="rowColor" value="one" />
		</c:otherwise>
	</c:choose>
	
	<div class="row ${rowColor} safari-fix">
		<div class="col-md-2 section-title text-left">
			<h3>${pubDate.start}<br>~<br>${pubDate.end}</h3>
		</div>
		
		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div id="story-${page.number}" class="story row">

				<c:forEach var="row" items="${page.content}" varStatus="cnt">
					<div class="item">

						<div class="item_title col-xs-12">
							<a href="${row.link}" target="_blank">${row.title}</a>
						</div>

						<c:if test="${not empty row.img}">
							<div>
								<img class="item_img" src="${row.img}" />
							</div>
						</c:if>
						<c:if test="${empty row.img}">
							<hr class="item_title-separator">
						</c:if>

						<div class="item_content col-xs-12"><span class="label label-primary article-sum-mark">${summary}</span>&nbsp;${row.mainContents}</div>



						<div class="row">
							<div class="panel-group panel-group-sArt col-xs-12">
								<div class="panel panel-default panel-sArt">

								<div class="panel-heading panel-heading-sArt">
									<h4 class="panel-title panel-${page.number}">
										<a class="btn btn-success pull-left" rel="tooltip" data-placement="top" title="Translate this!"  data-toggle="collapse"
											data-parent="#accordion" href="#collapse-trans-${page.number}-${cnt.index}"> 
											<span class="article-btn">Translate</span>
										</a>
										<a class="btn btn-success" href="${row.link}" target="_blank" rel="tooltip" title="Go to The Original~"> 
											<span class="article-btn">More</span>
										</a>
										<a class="btn btn-success pull-right <c:if test="${empty row.similarTitle}"> disabled </c:if>" rel="tooltip" data-placement="top" title="Related articles."  data-toggle="collapse"
											data-parent="#accordion" href="#collapse-${page.number}-${cnt.index}"> 
											<span class="article-btn">Related</span>
										</a>
									</h4>
								</div>

									<c:if test="${not empty row.similarTitle}">
										<div id="collapse-${page.number}-${cnt.index}"
											class="collapse-${page.number} panel-collapse collapse panel-collapse-sArt">
											<div class="panel-body panel-body-sArt">
											${row.similarSection}
											
											</div>
										</div>
									</c:if>

									<c:if test="${not empty row.transedContents}">
										<div id="collapse-trans-${page.number}-${cnt.index}"
											class="collapse-${page.number} panel-collapse collapse panel-collapse-sArt">
											<div class="panel-body panel-body-sArt">
												<div class="panel-article-info col-xs-12">
													${row.transedContents}
												</div>
											</div>
										</div>
									</c:if>

								</div>
							</div>
							<!-- panel -->
						</div>

					</div>

				</c:forEach>

			</div>
		</div>
		<!-- // article section -->
	</div>
	
	<script type="text/javascript">
		$('#story-${page.number}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});

		$('.collapse-${page.number}').on('shown.bs.collapse hidden.bs.collapse',
				function() {
					$('#story-${page.number}').each(function() {
						var $container = $(this);
						$container.imagesLoaded(function() {
							$container.packery({
								itemSelector : '.item',
								gutter : 5
							});
						});
					});
				});

		$("[rel='tooltip']").tooltip();

	</script>
</c:if>