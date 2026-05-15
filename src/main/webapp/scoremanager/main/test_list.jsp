<%-- 学生一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		得点管理システム
	</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>
<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">	
	<%-- 学生参照（科目） --%>	
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
							<option value="0">--------</option>
								<%-- 現在のyearと選択されていたf1が一致していた場合selectedを追記 --%>
							<c:forEach var="y" items="${ent_year_set}">
	    					<option value="${y}" <c:if test="${y == param.f1}">selected</c:if>>
	    					${y}</option>
							</c:forEach>
						</select>
					</div>
					<%-- クラス --%>
					<div class="col-2">
						<label class="form-label" for="student-f2-select">クラス</label>
						<select class="form-select" id="student-f2-select" name="f2">
							<option value="0">--------</option>
							<c:forEach var="num" items="${ class_num_set}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${num }" <c:if test="${num==param.class_num_set }">selected</c:if>>${num }</option>
							</c:forEach>
						</select>
					</div>
					<%-- 科目 --%>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">科目</label>
						<select class="form-select" id="student-f2-select" name="f3">
							<option value="0">--------</option>
							    <c:forEach var="sub" items="${subject_set}">
							        <option value="${sub.cd}"
							            <c:if test="${sub.cd == param.subject_set}">selected</c:if>>${sub.name}</option>
	    						</c:forEach>
						 </select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">検索</button>
					</div>
					<input  type="hidden" id="subject" value="sj" name="f"  />
					<br>
					<%-- エラー表示 --%>
					<p class="text-warning">${error.f1 }</p>					
			</form>	
						
				<hr class="my-4" />
		<%-- 学生参照（学生） --%>
		<div class="mb-3">
			<form action="TestListStudentExecute.action" method="get"
			 class="row gx-2 gy-2 align-items-center mt-3">
				
				<div class="col-2 text-center">
					<p>学生情報</p>
				</div>
				<div class="col-4">
					<label class="form-label" for="student-f4-text">学生番号</label>					
					<input class="form-control" type="text" id="student-f4-text" value="${f4}" 
					name="f4"required maxlength="10" placeholder="学生番号を入力してください" />							
				</div>
				<div class="col-2 text-center">
					<button class="btn btn-secondary" id="filter-button">検索</button>
				</div>
				<input  type="hidden" id="student" value="st" name="f"  />					
			</form>	
		</div>
		</div>				
</div>
	<%-- info --%>	
	<div>
	<label><p class="text-info"> 科目情報を選択または学生情報を入力して検索ボタンをクリックしてください</p></label>
		</div>	
	</c:param>
	
</c:import>