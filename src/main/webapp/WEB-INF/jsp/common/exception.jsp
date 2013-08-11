<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="includes.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ERROR</title>

<jsp:include page="../common/resources.jsp" />

<style type="text/css">
body {
	background-color: #fafafa;
	margin: 0;
	font-size: 13px;
	font-weight: normal;
	line-height: 18px;
	color: #404040;
}

.error-container {
  margin-top: 100px;
  text-align: center;
}
.error-container h1 {
  margin-bottom: .5em;
  font-size: 120px;
  line-height: 1em;
}
.error-container h2 {
  margin-bottom: .75em;
  font-size: 28px;
}
.error-container .error-details {
  margin-bottom: 1.5em;
  font-size: 16px;
}
.error-container .error-actions a {
  margin: 0 .5em;
}

</style>

</head>
<body>
	 
	<div class="container" >

		<div class="row">
			<div class="span12">
				<div class="error-container">
					<h1>Oops!</h1>

					<h2>${exception}</h2>

					<div class="error-details">${errMsg}</div>
					<!-- /error-details -->

					<div class="error-actions">
						<a href='<c:url value="/home"/>' class="btn btn-large btn-warning">
							<i class="icon-chevron-left"></i> &nbsp; Back to Home
						</a> 
						<a href="#" class="btn btn-large"> <i class="icon-envelope"></i>
							&nbsp; Contact Support
						</a>

					</div>
					<!-- /error-actions -->
				</div>
				<!-- /error-container -->
			</div>
			<!-- /span12 -->
		</div>
		<!-- /row -->

	</div>
</body>

</html>