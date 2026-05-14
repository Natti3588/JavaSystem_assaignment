package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    String entYearStr = "";
    String classNum = "";
    String subject = "";
    int entYear;
    LocalDate todaysDate = LocalDate.now();
    int year = todaysDate.getYear();

    List<TestListSubject> testlistsubject = null;


    TestListSubjectDao testlistsubjectDao = new TestListSubjectDao();
    SubjectDao subjectDao = new SubjectDao();
    ClassNumDao classnumDao = new ClassNumDao();
    Map<String, String> errors = new HashMap<>();


    // 入学年度を取得
    entYearStr = req.getParameter("f1");

    // クラスを取得
    classNum = req.getParameter("f2");

    // 科目名を取得
    subject = req.getParameter("f3");

    // ビジネスロジック4

    entYear = Integer.parseInt(entYearStr);


    // jspで1つでも未入力で検索されていた時
    if ("0".equals(entYearStr) || "0".equals(classNum) || "0".equals(subject)) {

      errors.put("f1", "入学年度とクラスと科目を選択してください");
      // リクエストにエラーメッセージをセット
      req.setAttribute("error", errors);
      // サーブレットに戻る
      req.getRequestDispatcher("TestList.action").forward(req, res);
    }
    // 入力された科目を取得
    Subject sub = subjectDao.get(subject, teacher.getSchool());

    // 成績一覧結果を取得
    testlistsubject = testlistsubjectDao.filter(entYear, classNum, sub, teacher.getSchool());

    // 入学年度一覧を取得
    List<Integer> entYearSet = new ArrayList<>();
    // 10年前から来年までの年度をリストに追加する
    for (int i = year - 10; i <= year + 1; i++) {
      entYearSet.add(i);
    }

    // リクエストに入学年度リストをセット
    req.setAttribute("f1", entYearSet);
    // jspで入力された値を属性にセット
    req.setAttribute("entyear", entYearStr);

    // リクエストにクラス番号リストをセット
    List<String> classList = classnumDao.filter(teacher.getSchool());
    req.setAttribute("f2", classList);
    // jspで入力された値を属性にセット
    req.setAttribute("classnum", classNum);

    // リクエストに教科リストをセット
    List<Subject> subjectList = subjectDao.filter(teacher.getSchool());
    req.setAttribute("f3", subjectList);
    // jspで入力された値を属性にセット
    req.setAttribute("subjectname", sub.getName());



    // リクエストに成績一覧結果をセット
    req.setAttribute("subjectResults", testlistsubject);
    // 成績一覧結果が空の時
    if (testlistsubject.isEmpty()) {
      errors.put("f1", "学生情報が存在しませんでした");
      // リクエストにエラーメッセージをセット
      req.setAttribute("error", errors);
      req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);

    }
    req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
  }
}
