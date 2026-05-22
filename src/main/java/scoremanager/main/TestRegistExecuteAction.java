package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistExecuteAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    // セッションと学校を取得
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    School school = teacher.getSchool();

    // Mapを定義
    // inputPoints: 学生番号ごとに点数を取得
    // errors : 学生番号ごとにエラーを表示
    Map<String, String> inputPoints = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    // DB保存用のリストを宣言
    List<Test> saveList = new ArrayList<>();

    // フォームから検索条件を取得
    String entYearString = req.getParameter("f1");
    String classNumString = req.getParameter("f2");
    String subjectCd = req.getParameter("subject");
    String countString = req.getParameter("count");
    String[] studentNos = req.getParameterValues("regist");

    // 文字列を数値に変換
    int entYear = Integer.parseInt(entYearString);
    int count = Integer.parseInt(countString);

    // 科目コードから科目情報を取得
    Subject subject = new SubjectDao().get(subjectCd, school);

    // 対象の学生・テスト一覧を取得
    TestDao testDao = new TestDao();
    List<Test> tests = testDao.filter(entYear, classNumString, subject, count, school);

    // 学生ごとに点数を検証して保存リストを構築
    for (Test test : tests) {
      // テストから学生情報を取得
      Student student = test.getStudent();
      String studentNo = student.getNo();

      

      //registに含まれているかチェック
//      registで送られてきた学生番号が現在の学生番号studentNoと一致するかチェック
      boolean exists = false;
      for (String no : studentNos) {
          if (no.equals(studentNo)) {
              exists = true;
              break;
          }
      }
//      一致しなかったらスキップ
      if (!exists) continue;

      // 入力された点数をセット（学生番号別）
      String pointStr = req.getParameter("point_" + studentNo);

      // 数値変換チェック
      int point;
      try {
        point = Integer.parseInt(pointStr);
      } catch (NumberFormatException e) {
        errors.put(studentNo, "０～１００の値を入力してください");
        continue;
      }

      // ０～１００以外ならエラーをセット
      if (point < 0 || point > 100) {
        errors.put(studentNo, "０～１００の値を入力してください");
        inputPoints.put(studentNo, test.getPoint() == -1 ? "" : String.valueOf(test.getPoint()));
        continue;
      }

      // 保存に必要な情報をセット
      test.setPoint(point);
      test.setSchool(school);
      test.setSubject(subject);
      test.setNo(count);
      test.setStudent(student);
      saveList.add(test);
    }



    // エラーがあったら入力画面に戻す
    if (!errors.isEmpty()) {

      // 入学年度のプルダウン用
      int year = LocalDate.now().getYear();
      List<Integer> entYearSet = new ArrayList<>();
      for (int i = year - 10; i <= year + 1; i++)
        entYearSet.add(i);

      // 回数のプルダウン用
      List<String> numSet = new ArrayList<>();
      for (int i = 1; i <= 2; i++)
        numSet.add(String.valueOf(i));

      // エラー情報と入力値をセット
      req.setAttribute("errors", errors);
      req.setAttribute("input_points", inputPoints);

      // 一覧表示と検索条件をセット
      req.setAttribute("tests", tests);
      req.setAttribute("subject", subject);
      req.setAttribute("num", count);
      req.setAttribute("ent_year_set", entYearSet);
      req.setAttribute("class_num_set", new ClassNumDao().filter(school));
      req.setAttribute("num_set", numSet);
      req.setAttribute("subjects", new SubjectDao().filter(school));
      req.setAttribute("f1", entYearString);
      req.setAttribute("f2", classNumString);
      req.setAttribute("f3", subjectCd);
      req.setAttribute("f4", countString);

      // 入力画面に戻す
      req.getRequestDispatcher("test_regist.jsp").forward(req, res);
      return;
    }

    // DB保存を実行
    boolean isSuccess = testDao.save(saveList);

    // 成功なら完了画面、失敗ならコンソールにエラー出力
    if (isSuccess) {
      req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
      return;
    }
  }

}