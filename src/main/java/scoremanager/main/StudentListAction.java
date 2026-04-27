package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentListAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");

    String entYearStr = "";
    String classNum = "";
    String isAttendStr = "";
    int entYear = 0;
    boolean isAttend = false;
    List<Student> students = null;
    LocalDate todaysDate = LocalDate.now();
    int year = todaysDate.getYear();

    StudentDao studentDao = new StudentDao();
    ClassNumDao classNumDao = new ClassNumDao();
    Map<String, String> errors = new HashMap<>();

    // 表示を選択してるだけ

    // 入学年度を取得
    entYearStr = req.getParameter("f1");

    // クラスを取得
    classNum = req.getParameter("f2");

    // 在学しているかどうか取得
    isAttendStr = req.getParameter("f3");

    // ビジネスロジック4
    if (entYearStr != null) {
      entYear = Integer.parseInt(entYearStr);
    }

    // 在学に記入があればTrue
    if (isAttendStr != null) {
      isAttend = true;
    }

    List<Integer> entYearSet = new ArrayList<>();

    // 10年前から一年後まで年をリストに追加
    for (int i = year - 10; i < year + 1; i++) {
      entYearSet.add(i);
    }

    // DBからデータ取得
    List<String> list = classNumDao.filter(teacher.getSchool());

    if (entYear != 0 && !classNum.equals("0")) {
      // 入学年度とクラス番号を指定
      students = studentDao.filter(teacher.getSchool(), entYear, classNum, isAttend);

    } else if (entYear != 0 && classNum.equals("0")) {
      // 入学年度のみ指定
      students = studentDao.filter(teacher.getSchool(), entYear, isAttend);

    } else if (entYear == 0 && classNum == null || entYear == 0 && classNum.equals("0")) {
      // 指定なし
      // 全学生情報を取得
      students = studentDao.filter(teacher.getSchool(), isAttend);

    } else {
      errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
      // リクエストにエラーメッセージをセット
      req.setAttribute("errors", errors);

      // 全学生情報を取得
      students = studentDao.filter(teacher.getSchool(), isAttend);
    }

    // リクエストに入学年度をセット
    req.setAttribute("f1", entYear);

    // リクエストにクラス番号をセット
    req.setAttribute("f2", classNum);

    // 在学フラグが送信されていた場合
    if (isAttendStr != null) {
      req.setAttribute("f3", isAttendStr);
    }

    // リクエストに学生リストをセット
    req.setAttribute("students", students);

    req.setAttribute("class_num_set", list);
    req.setAttribute("ent_year_set", entYearSet);

    req.getRequestDispatcher("student_list.jsp").forward(req, res);

  }
}
