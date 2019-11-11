package pfg.firstmarket.control.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Book;
import pfg.firstmarket.model.Category;

public class DeleteCategoryAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Category deleted_category = new Category (request.getParameter("category_id"), null);
		Category parent_category = FrontController.cs.getParent(deleted_category);//categoria padre de la que se pretende eliminar
		
		if (request.getParameter("deleteSubtree") == null) {
			//actualizar el campo categoria de los libros asociados a la categoria que se pretende eliminar
			riseBookCategories(deleted_category, parent_category);
			//eliminar la categoria
			FrontController.db.deleteCategory(deleted_category);
		}
		else {//actualizar el campo categoria de los libros asociados a laS categoriaS que se pretendeN eliminar
			List<Category> afected_categories = FrontController.cs.getDescendants(deleted_category);
			for (Category afected_category : afected_categories) {
				riseBookCategories(afected_category, parent_category);
			}
			//
			FrontController.db.deleteSubCategories(deleted_category);
		}
	}
	
	private void riseBookCategories (Category old_category, Category new_category) {
		//obtener la lista de libros afectados
		List<String> conditions = new ArrayList<String>();
		conditions.add("category_id=" + old_category.getCategory_id());
		List<Book> afected_books = FrontController.db.getBooks(conditions);
		//actualizar los libros
		for (Book afected_book : afected_books) {
			afected_book.setCategory(new_category);
			FrontController.db.updateBook(afected_book);
		}
	}

}
