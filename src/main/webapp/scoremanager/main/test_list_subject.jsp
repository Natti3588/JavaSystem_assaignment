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
	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>
<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
	<div class="mb-3">
		<form action="TestListSubjectExecute.action" method="get"
		 class="row gx-2 gy-2 align-items-center mt-3">
				<div class="col-2 text-center">
				<p>科目情報</p>
				</div>
				<%-- 入学年度 --%>
					<div class="col-2">
						<label class="form-label" for="student-f1-select">入学年度</label>
						<select class="form-select" id="student-f1-select" name="f1">
							<option value="0">${entyear }</option>
								<%-- 現在のyearと選択されていたf1が一致していた場合selectedを追記 --%>
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
							<option value="0">${classnum }</option>
							<c:forEach var="num" items="${ f2}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${num }" <c:if test="${num==param.f2 }">selected</c:if>>${num }</option>
							</c:forEach>
						</select>
					</div>
				<%-- 科目 --%>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">科目</label>
						<select class="form-select" id="student-f2-select" name="f3">
							<option value="0">${subjectname}</option>
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
				<p>学生情報</p>
				</div>
				<%-- 学生番号 --%>
					<div class="col-4">
						<label class="form-label" for="student-f4-text">学生番号</label>
						<input class="form-control" type="text" id="student-f4-text" value="${studentno}" 
						name="f4"required maxlength="10" placeholder="学生番号を入力してください" />
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">検索</button>
					</div>	
		</form>
	</div>
	</div>
</div>		
		<%-- エラー表示 --%>
			<div>
			<p>${error.f1 }</p>
			</div>
		<%-- 成績情報 --%>
			
<div class="mb-5">	
<c:if test="${not empty subjectResults}">		
<p class="mb-3">
    科目：${subjectname}
</p>

<div class="table-responsive">
    <table class="table table-hover">
        <thead>
            <tr>
                <th>入学年度</th>
                <th>クラス</th>
                <th>学生番号</th>
                <th>氏名</th>
                <th>1回</th>
                <th>2回</th>
            </tr>
        </thead>
        <tbody>
<%-- TestListSubjectExecuteAction.javaから取得し、テーブル表示 --%>
<c:forEach var="item" items="${subjectResults}">

    <tr>
        <td><c:out value="${item.entYear}" /></td><%-- 入学年度 --%>
        <td><c:out value="${item.classNum}" /></td><%-- クラス番号 --%>
        <td><c:out value="${item.studentNo}" /></td><%-- 学生番号 --%>
        <td><c:out value="${item.studentName}" /></td><%-- 氏名 --%>
		<td>
    		<c:choose>
		        <c:when test="${item.getPoint(1) == 'null'}">-</c:when><%-- 1回目のテストを受けてなかったら点数を"-" --%>
		        <c:otherwise>${item.getPoint(1)}</c:otherwise><%-- 1回目の点数 --%>
		    </c:choose>
		</td>
		<td>
		    <c:choose>
		        <c:when test="${item.getPoint(2) == 'null'}">-</c:when><%-- 2回目のテストを受けてなかったら点数を"-" --%>
		        <c:otherwise>${item.getPoint(2)}</c:otherwise><%-- 2回目の点数 --%>
		    </c:choose>
		</td>
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

<%--条件分岐--%>
<%-- https://segakuin.com/java/jsp/jstl/choose.html --%>
