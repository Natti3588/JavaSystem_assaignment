package scoremanager.main;

import bean.Student;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    // Studentインスタンスを宣言
    Student student = new Student();

    // 更新のための値をセット
    student.setNo(req.getParameter("no"));
    student.setName(req.getParameter("name"));
    student.setClassNum(req.getParameter("class_num"));
    student.setAttend(req.getParameter("is_attend") != null); // CheckBoxにCheckが入っていたらtrueを返す

    // studentDaoインスタンスを宣言
    StudentDao studentDao = new StudentDao();

    // Daoでセットした値を保存
    studentDao.save(student);

    // student_update_done.jspにフォワード
    req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
  }

}
