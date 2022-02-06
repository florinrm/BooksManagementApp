package com.book.demo.persistance;

import com.book.demo.domain.Book;
import com.book.demo.domain.OperationLog;
import org.springframework.data.repository.CrudRepository;

public interface OperationLogRepository extends CrudRepository<OperationLog, String> {
}
