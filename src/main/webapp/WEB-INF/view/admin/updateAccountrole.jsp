<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">
			<jsp:include page="../fragments/menu.jsp" />

		</div>
	</nav>





 
	<div>

		<c:if test="${not empty msg}">
			<div class="alert alert-success" role="alert">${msg}</div>
		</c:if>

		<f:form action="${pageContext.request.contextPath}/admin/updateAccount"
			method="POST" modelAttribute="AccountModel">
			 

 
 
			<div class="row">
				<div class="col">
					<label>Login</label>
						<f:input path="username" type="text" class="form-control"
						placeholder="username" />
					<f:errors path="username" class="text-danger" />
				</div>
				</div>
			 

			<div class="row">
				 <div class="col">
					<label>Role</label>
					<f:input path="roleId" type="text" class="form-control"
						placeholder="roleId" />
					<f:errors path="roleId" class="text-danger" />
				 </div>

			</div>
 



			<div style="text-align: right">
				<button type="submit" class="btn btn-primary">Update</button>
				<button type="reset" class="btn btn-secondary">Rest</button>
			</div>

		 </f:form>
	</div>

</div>

	<jsp:include page="../fragments/adminfooter.jsp" />