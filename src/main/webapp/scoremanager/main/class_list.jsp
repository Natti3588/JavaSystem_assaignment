<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
	<c:param name="title" value="得点管理システム" />
	<c:param name="scripts"></c:param>
	<c:param name="content">
	
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>
			<div class="my-2 text-end px-4">
				<a href="ClassCreate.action">新規登録</a>
			</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th>クラス番号</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="class_num" items="${class_num_set}">
								<tr class="align-middle">
									<td>${class_num}</td>
									<td><a href="ClassUpdate.action?num=${class_num}">変更</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
		</section>
	</c:param>
</c:import>