<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="./includes.jsp"%>

<c:if test="${end == true}">end</c:if>

<c:forEach var="entry" items="${articleMap}" varStatus="loop">

	<c:choose>
		<c:when test="${rowColor == 'one'}">
			<c:set var="rowColor" value="two" />
		</c:when>
		<c:otherwise>
			<c:set var="rowColor" value="one" />
		</c:otherwise>
	</c:choose>

	<div class="row ${rowColor} safari-fix">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div id="story-${requestDay}-${entry.key}" class="story row">

				<c:forEach var="row" items="${entry.value}" varStatus="cnt">

                    <c:if test="${row.numDups >= 3}"><div class="item" style="border: 2px solid #d2322d"></c:if>
                    <c:if test="${row.numDups < 3}"><div class="item"></c:if>

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

                        <!-- sharing buttons -->
                        <div class="container-fluid social-btn">
                            <ul class="nav navbar-nav row social-btn-bar">
                                <li class="col-xs-2">
                                    <spring:url value="https://www.facebook.com/sharer/sharer.php" var="facebook">
                                        <spring:param name="u" value="${row.link}"/>
                                    </spring:url>
                                    <a href="${facebook}" target="_blank" title="Facebook">
                                        <img src="<c:url value="/img/social/Facebook.png"/>">
                                    </a>
                                </li>
                                <li class="col-xs-2">
                                    <spring:url value="https://twitter.com/intent/tweet" var="twitter">
                                        <spring:param name="source" value="${row.link}"/>
                                        <spring:param name="text" value="${row.title}: ${row.link}"/>
                                    </spring:url>
                                    <a href="${twitter}" target="_blank" title="Tweet">
                                        <img src="<c:url value="/img/social/Twitter.png"/>">
                                    </a>
                                </li>
                                <li class="col-xs-2">
                                    <spring:url value="https://plus.google.com/share" var="google">
                                        <spring:param name="url" value="${row.link}"/>
                                    </spring:url>
                                    <a href="${google}" target="_blank" title="Share on Google+">
                                        <img src="<c:url value="/img/social/Google+.png"/>">
                                    </a>
                                </li>
                                <li class="col-xs-2">
                                    <spring:url value="http://www.tumblr.com/share" var="tumblr">
                                        <spring:param name="v" value="3"/>
                                        <spring:param name="u" value="http%3A%2F%2F${fn:replace(row.link,'http://','')}"/>
                                        <spring:param name="t" value="${row.title}"/>
                                        <spring:param name="s" value=" "/>
                                    </spring:url>
                                    <a href="${tumblr}" target="_blank" title="Post to Tumblr">
                                        <img src="<c:url value="/img/social/Tumblr.png"/>">
                                    </a>
                                </li>
                                <li class="col-xs-2">
                                    <spring:url value="https://getpocket.com/save" var="getpocket">
                                        <spring:param name="url" value="http%3A%2F%2F${fn:replace(row.link,'http://','')}"/>
                                        <spring:param name="title" value="${row.title}"/>
                                    </spring:url>
                                    <a href="${getpocket}" target="_blank" title="Add to Pocket">
                                        <img src="<c:url value="/img/social/Pocket.png"/>">
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <!-- // sharing buttons -->

						<div class="row">
							<div class="panel-group panel-group-sArt col-xs-12">
								<div class="panel panel-default panel-sArt">

								<div class="panel-heading panel-heading-sArt">
									<h4 class="panel-title panel-${entry.key}">
										<a class="btn btn-success pull-left" data-toggle="collapse"
											data-parent="#accordion" href="#collapse-text-${entry.key}-${cnt.index}">
											<span class="article-btn">${contents}</span>
										</a>
                                        <a class="btn btn-success" href="${row.link}" target="_blank" >
                                            <span class="article-btn">${more}</span>
                                        </a>
										<a class="btn btn-success pull-right <c:if test="${empty row.similarTitle}"> disabled </c:if>" data-toggle="collapse"
											data-parent="#accordion" href="#collapse-${entry.key}-${cnt.index}"> 
											<span class="article-btn">${related}</span>
										</a>
									</h4>
								</div>

                                <div id="collapse-text-${entry.key}-${cnt.index}"
                                     class="collapse-${requestDay}-${entry.key} panel-collapse collapse panel-collapse-sArt js-contents-${requestDay}-${entry.key}"
                                     targetId="collapse-text-${entry.key}-${cnt.index}" articleId="${row.id}">
                                    <div class="panel-body panel-body-sArt">
                                        <div class="panel-article-info col-xs-12"></div>
                                    </div>
                                </div>

                                <c:if test="${not empty row.transedContents}">
                                <div id="collapse-${entry.key}-${cnt.index}"
                                    class="collapse-${requestDay}-${entry.key} panel-collapse collapse panel-collapse-sArt">
                                    <div class="panel-body panel-body-sArt">
                                        ${row.similarSection}
                                    </div>
                                </div>
                                </c:if>

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
        // align all articles
		$('#story-${requestDay}-${entry.key}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});

        // align articles for each time row
        $('.collapse-${requestDay}-${entry.key}').on('shown.bs.collapse hidden.bs.collapse', function () {
            $('#story-${requestDay}-${entry.key}').each(function () {
                var $container = $(this);
                $container.imagesLoaded(function () {
                    $container.packery({
                        itemSelector: '.item',
                        gutter: 5
                    });
                });
            });
        });

        // when clicking the btn, get the original text of the article
        $('.js-contents-${requestDay}-${entry.key}').on('show.bs.collapse', function () {
            var targetId = $(this).attr("targetId");
            var articleId = $(this).attr("articleId");
            var reqURL = "<c:url value="/kr/article/"/>" + articleId;

            $.ajax({
                type: "GET",
                url: reqURL
            }).done(function (json) {
                if (json.status != "SUCCESS") {
                    alert(json.cause);
                }

                $("#" + targetId).find(".panel-article-info").html(json.contents);
            }).error(function (response) {
                alert("[ERROR] " + response.status + " : " + response.statusText);
            }).always(function () {
            });
        });

	</script>
</c:forEach>