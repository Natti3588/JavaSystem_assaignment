<%-- 成績参照SP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>
	<%-- 内容 --%>
	<c:param name="content">
	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（学生）</h2>
<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
	<div class="mb-3">
		<form action="TestListSubjectExecute.action" method="get"
		 class="row gx-2 gy-2 align-items-center mt-3">
				<div class="col-2">
				<p>科目情報</p>
				</div>
				<%-- 入学年度 --%>
					<div class="col-2">
						<label class="form-label" for="student-f1-select">入学年度</label>
						<select class="form-select" id="student-f1-select" name="f1">
							<option value="0">--------</option>
							<c:forEach var="y" items="${f1}">
    						<option value="${y}" <c:if test="${y == param.f1}">selected</c:if>>
						        ${y}
						    </option>
							</c:forEach>
						</select>
					</div>
				<%-- クラス --%>
					<div class="col-2">
						<label class="form-label" for="student-f2-select">クラス</label>
						<select class="form-select" id="student-f2-select" name="f2">
							<option value="0">--------</option>
							<c:forEach var="num" items="${ f2}">
								<option value="${num }" <c:if test="${num==param.f2 }">selected</c:if>>${num }</option>
							</c:forEach>
						</select>
					</div>
				<%-- 科目 --%>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">科目</label>
						<select class="form-select" id="student-f2-select" name="f3">
							<option value="0">--------</option>
							    <c:forEach var="sub" items="${f3}">
							        <option value="${sub.cd}"
							            <c:if test="${sub.cd == param.f3}">selected</c:if>>
							            ${sub.name}
							</option>
    							</c:forEach>
						 </select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">検索</button>
					</div>
			</form>				
			<hr class="my-4" />
	<div class="mb-3">
		<form action="TestListStudentExecute.action" method="get"
		 class="row gx-2 gy-2 align-items-center mt-3">
				<div class="col-2 text-center">
				<p>科目情報</p>
				</div>
				<%-- 学生番号 --%>
					<div class="col-4">
						<label class="form-label" for="student-f4-text">学生番号</label>
						<input class="form-control" type="text" id="student-f4-text" value="${search_stu.no}" 
						name="f4"required maxlength="10"  />
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">検索</button>
					</div>	
		</form>
	</div>
	</div>
</div>		
		
		
			
<div class="mb-5">
		
<div>
<p class="mb-3">
    氏名：${search_stu.name}（${search_stu.no }）
</p></div>
<%--エラー表示 --%>
<div>${error.f1 }	</div>
	

<%-- 成績情報 --%>
<c:if test="${not empty studentResults}">	
<div class="table-responsive">
    <table class="table table-hover">
        <thead>
            <tr>
                <th>科目名</th>
                <th>科目コード</th>
                <th>回数</th>
                <th>点数</th>
                
            </tr>
        </thead>
        <tbody>
<%-- TestListStudentExecuteAction.javaから取得し、テーブル表示 --%>
<c:forEach var="item" items="${studentResults}">

    <tr>
        <td><c:out value="${item.subjectName}" /></td><%-- 科目名 --%>
        <td><c:out value="${item.subjectCd}" /></td><%-- 科目コード --%>
		<td><c:out value="${item.num}" /><%--回数 --%>
		<td><c:out value="${item.point}" /></td><%--点数 --%>
    </tr>
</c:forEach>
        </tbody>
    </table>
</div>
</div>
</c:if>
	</c:param>
<%-- 内容終了 --%>
</c:import>