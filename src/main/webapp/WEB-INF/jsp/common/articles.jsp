<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="./includes.jsp"%>

<c:forEach var="entry" items="${articleMap}" varStatus="loop">

	<c:choose>
		<c:when test="${rowColor == 'one'}">
			<c:set var="rowColor" value="two" />
		</c:when>
		<c:otherwise>
			<c:set var="rowColor" value="one" />
		</c:otherwise>
	</c:choose>

	<div class="row ${rowColor}">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div id="story-${entry.key}" class="story row">

				<c:forEach var="row" items="${entry.value}" varStatus="cnt">
					<div class="item">

						<div class="caption">
							<div class="panel-heading panel-heading-sArt">
								<h4 class="panel-title panel-${entry.key}">
									<a href="${row.link}" target="_blank" class="label label-default" rel="tooltip" title="Go to The Original~"> 
									<span class="glyphicon glyphicon-share-alt"></span>
									</a>
								</h4>
							</div>

							<div class="panel-heading panel-heading-sArt">

								<h4 class="panel-title panel-${entry.key}">
									<a rel="tooltip" data-placement="right" title="Translate this!" class="label label-default" data-toggle="collapse"
										data-parent="#accordion" href="#collapse-trans-${entry.key}-${cnt.index}"> 
										<span class="glyphicon glyphicon-transfer"></span>
									</a>
								</h4>

							</div>
					<!-- 
							<div class="panel-heading panel-heading-sArt">

								<h4 class="panel-title panel-${entry.key}">
									<a rel="tooltip" data-placement="left" title="See later~" class="label label-default js-add-article" href="#"> <span
										class="glyphicon glyphicon-plus icon-disabled"></span>
									</a>
								</h4>

							</div>
 					-->

							<div class="panel-heading panel-heading-sArt">

								<h4 class="panel-title panel-${entry.key}">
									<a rel="tooltip" data-placement="bottom" title="Related articles." class="label label-default" data-toggle="collapse"
										data-parent="#accordion" href="#collapse-${entry.key}-${cnt.index}"> 
										<span class="glyphicon glyphicon-list <c:if test="${empty row.simularList}"> icon-disabled </c:if>"></span>
									</a>
								</h4>

							</div>

						</div>



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

						<div class="item_content col-xs-12">${row.mainContents}</div>



						<div class="row">
							<div class="panel-group panel-group-sArt col-xs-12">
								<div class="panel panel-default panel-sArt">



									<c:if test="${not empty row.simularList}">
										<div id="collapse-${entry.key}-${cnt.index}"
											class="collapse-${entry.key} panel-collapse collapse panel-collapse-sArt">
											<div class="panel-body panel-body-sArt">
												<c:forEach var="sArticle" items="${row.simularList}">
													<div class="panel-article-info col-xs-12">
														<a href="${sArticle.link}" target="_blank">${sArticle.title}</a>
													</div>
												</c:forEach>
											</div>
										</div>
									</c:if>

									<c:if test="${not empty row.transedContents}">
										<div id="collapse-trans-${entry.key}-${cnt.index}"
											class="collapse-${entry.key} panel-collapse collapse panel-collapse-sArt">
											<div class="panel-body panel-body-sArt">
												<div class="panel-article-info col-xs-12">${row.transedContents}</div>
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
		$('#story-${entry.key}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});

		$('.collapse-${entry.key}').on('shown.bs.collapse hidden.bs.collapse',
				function() {
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

		$("[rel='tooltip']").tooltip();

		$('.item').hover(function() {
			$(this).find('.caption').slideDown(250); //.fadeIn(250)
		}, function() {
			$(this).find('.caption').slideUp(250); //.fadeOut(205)
		});
	</script>
</c:forEach>