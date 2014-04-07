<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="./includes.jsp"%>

<c:if test="${end == true}">
end
</c:if>

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
			<div id="story-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key}" class="story row">

				<c:forEach var="row" items="${entry.value}" varStatus="cnt">
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

						<div class="item_content col-xs-12"><span class="label label-primary article-sum-mark">요약</span>&nbsp;${row.mainContents}</div>



						<div class="row">
							<div class="panel-group panel-group-sArt col-xs-12">
								<div class="panel panel-default panel-sArt">

								<div class="panel-heading panel-heading-sArt">
									<h4 class="panel-title panel-${entry.key}">
										<a class="btn btn-success pull-left" href="${row.link}" target="_blank" rel="tooltip" title="Go to The Original~"> 
										<span class="article-btn">원문</span>
										</a>
										
										<a class="btn btn-success" rel="tooltip" data-placement="top" title="Translate this!"  data-toggle="collapse"
										data-parent="#accordion" href="#collapse-trans-${entry.key}-${cnt.index}"> 
										<span class="article-btn">번역</span>
										</a>
										
										<!--
										<a rel="tooltip" data-placement="left" title="See later~" class="js-add-article" href="#"> <span
											class="glyphicon glyphicon-plus icon-disabled"></span>
										</a>
									 	-->
									 	
										<a class="btn btn-success pull-right <c:if test="${empty row.simularList}"> disabled </c:if>" rel="tooltip" data-placement="top" title="Related articles."  data-toggle="collapse"
											data-parent="#accordion" href="#collapse-${entry.key}-${cnt.index}"> 
											<span class="article-btn">관련</span>
										</a>
									</h4>
								</div>

									<c:if test="${not empty row.simularList}">
										<div id="collapse-${entry.key}-${cnt.index}"
											class="collapse-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key} panel-collapse collapse panel-collapse-sArt">
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
											class="collapse-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key} panel-collapse collapse panel-collapse-sArt">
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
		$('#story-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});

		$('.collapse-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key}').on('shown.bs.collapse hidden.bs.collapse',
				function() {
					$('#story-<fmt:formatDate pattern="yyyy-dd-MM" value="${requestDay}"/>-${entry.key}').each(function() {
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
</c:forEach>