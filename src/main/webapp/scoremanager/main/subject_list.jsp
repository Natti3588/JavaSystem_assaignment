<%-- 科目管理JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>



    <c:param name="content">
        <section class="me-4 ">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目管理</h2>

            <div class="my-2 text-end">
                <a href="SubjectCreate.action">新規登録</a>
            </div>

            <%-- 上部：科目情報テーブル（画像参照） --%>
            <table class="table " >
                    <tr>
                        <th class="col-3">科目コード</th>
                        <th>科目名</th>
                        <th class="col-2"></th>
                        <th class="col-2" ></th>
                    </tr>
                <tbody>
                  <c:forEach var="subject" items="${subjects }"> 
                
                    <tr>
                        <td>${subject.cd}</td>
                        <td>${subject.name}</td>
                        <td><a href="SubjectUpdate.action?cd=${subject.cd }">変更</a></td>
          				<td><a href="SubjectDelete.action?cd=${subject.cd }">削除</a></td>
                    </tr>
                 </c:forEach> 
                    
           
				
                </tbody>
            </table>

         </section>
    </c:param>
</c:import>