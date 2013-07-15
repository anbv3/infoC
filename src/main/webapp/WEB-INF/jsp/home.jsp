<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>

<jsp:include page="./common/include-resources.jsp" />


<style type="text/css">
.item {
	width: 30%;
	margin-bottom: 5px;
	text-align: justify;
	word-wrap: break-word;
	
	border-width: 1px;
	border-style: solid;
	border-color: #a0a0a0 #e0e4e6 #e0e4e6;
}

.item.w2 {
	width: 30%;
}

.item_title {
padding: 7px 12px 7px 20px;
border-bottom: 1px solid #e1e1e1;
background-color: #f4f6f7;
}

.item_content {
padding: 11px 18px 20px 20px;
background-color: #fbfbfb;
}

.item_link {
padding: 7px 12px 7px 20px;
border-bottom: 1px solid #e1e1e1;
background-color: #f3f6fb;
}


.color-strip {
	background: #ccc url('img/color-strip.png') no-repeat 20% top;
	height: 5px;
}

#top-section {
	margin-top: 40px;
	margin-bottom: 0;
}

.one {
	margin: 0 auto;
	padding: 20px 0 20px 0;
	color: #000;
}

.two {
	background: #333;
	margin: 0 auto;
	padding: 20px 0 20px 0;
}
</style>

<script type="text/javascript">
<!--

$(function() {
	
	$("#ttt").on("click", function() {
		test();
       });
	
	$('.story').each(function() {
		var $container = $(this);
		$container.imagesLoaded(function() {
			//$container.packery({
			$container.packery({
				itemSelector: '.item',
				gutter: 15
			});
		});
	});
	
});
//-->
</script>

</head>


<body>
	<!-- header -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="index.html">InfoC</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="#">Home</a></li>
						<li class="active"><a href="#">IT</a></li>
						<li><a href="#">Contact</a></li>

					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
		<div class="color-strip"></div>
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<!-- body -->
	
	<div id="top-section" class="well well-large">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12 text-center">
					<h3>
						<strong>LOREM IPSUM DOLOR</strong> - 
						<span><em>Curabitur viverra nulla non tellus suscipit condimentum eget.</em></span>
					</h3>
				</div>
			</div>
		</div>
	</div>

	<div class="one">
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">

					<c:forEach var="row" items="${articleList}" varStatus="cnt">
						<div class="item w2">
							<div class="item_title">${row.title}</div>
							<div class="item_content">${row.contents}</div>
							<div class="item_link"><a href="${row.link}" target="_blank">${row.link}</a></div>
						</div>
					</c:forEach>
						
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>

	<div class="two">
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">
					
						<c:forEach var="row" items="${articleList2}" varStatus="cnt">
						<c:if test="${(cnt.count % 4) == 0}">
						<div class="item w2">
							<div class="item_title">${row.getTitle()}</div>
							<div class="item_content">${row.getDescription().getValue()}</div>
							<div class="item_link">${row.getLink()}</div>
						</div>
						</c:if>
						
						<c:if test="${not ((cnt.count % 4) == 0)}">
						<div class="item">
							<div class="item_title">${row.getTitle()}</div>
							<div class="item_content">${row.getDescription().getValue()}</div>
							<div class="item_link">${row.getLink()}</div>
						</div>
						</c:if>
					
					</c:forEach>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>


	<div class="one">
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div id="ccc" class="story">
						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item w2">
							<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg" />
						</div>
						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item w2">
							<table border="0" cellpadding="2" cellspacing="7" style="vertical-align: top;">
								<tr>
									<td width="80" align="center" valign="top"><font style="font-size: 85%; font-family: arial, sans-serif"></font></td>
									<td valign="top" class="j"><font style="font-size: 85%; font-family: arial, sans-serif"><br />
										<div style="padding-top: 0.8em;">
												<img alt="" height="1" width="1" />
											</div>
											<div class="lh">
												<a href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGiTaZOPsGV45cX7FbCgwHiaI2adQ&amp;url=http://news.hankooki.com/lpage/politics/201307/h2013070821045121000.htm"><b>초강경
														발언… 막말 논란… 여야 충돌</b></a><br />
												<font size="-1"><b><font color="#6f6f6f">한국일보</font></b></font><br />
												<font size="-1">국정원의 대선 개입 의혹 및 2007년 남북정상회담 대화록 공개를 둘러싼 여야의 공방이 다시 가열되고 있다. 특히 민주당이 장외집회에서 위험 수위를
													넘나드는 초강경 발언을 쏟아내자 새누리당이 이에 대해 맹비난을 퍼붓는 등 여야가 정면 충돌했다. 민주당은&nbsp;<b>...</b>
												</font><br />
												<font size="-1"><a
													href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGUv_BnSD4X9erUr_KcsxteaE154g&amp;url=http://www.cctoday.co.kr/news/articleView.html?idxno%3D781031">野,
														장외투쟁에 與 “의회정치를”</a><font size="-1" color="#6f6f6f"><nobr>충청투데이</nobr></font></font><br />
												<font size="-1"><a
													href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNH0300yGdiWn7aqp4gWLU83OMicSQ&amp;url=http://news.sbs.co.kr/section_news/news_read.jsp?news_id%3DN1001872529">새누리
														&quot;민주, &#39;자해공갈 협박&#39;으로 국민 외면 자초&quot;</a><font size="-1" color="#6f6f6f"><nobr>SBS뉴스</nobr></font></font><br />
												<font size="-1"><a
													href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNHK6a9jykYk89F7dBfc_YQl2Zd_5w&amp;url=http://www.newsis.com/ar_detail/view.html?ar_id%3DNISX20130708_0012212014%26cID%3D10301%26pID%3D10300">[종합]與,
														대선무효 투쟁발언에 발끈 &quot;민주 자해공갈단&quot;</a><font size="-1" color="#6f6f6f"><nobr>뉴시스</nobr></font></font><br />
												<font size="-1" class="p"><a
													href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNHbz8355yEq5P5iL-EapBkzANA8Ig&amp;url=http://imnews.imbc.com/news/2013/politic/article/3307134_11199.html"><nobr>MBC뉴스</nobr></a>&nbsp;-<a
													href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNF2ky-6N5Ag5AlKS_Z9aGAUCHPj5w&amp;url=http://www.yonhapnews.co.kr/politics/2013/07/08/0502000000AKR20130708066651001.HTML"><nobr>연합뉴스</nobr></a></font><br />
												<font class="p" size="-1"><a class="p"
													href="http://news.google.co.kr/news/story?ncl=djbZNJA1HG9fWgM3IPMgvCp0YwxpM&amp;ned=kr&amp;topic=p"><nobr>
															<b>전체뉴스 16개&nbsp;&raquo;</b>
														</nobr></a></font>
											</div>
											</font>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>


</body>

</html>