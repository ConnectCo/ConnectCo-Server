package com.connectCo.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3FileComponentTest {

    AmazonS3Client s3 = mock(AmazonS3Client.class);
    S3FileComponent component = new S3FileComponent(s3);
    static final String URL = "https://bucket.s3.ap-northeast-2.amazonaws.com/event/a.png";

    {
        ReflectionTestUtils.setField(component, "bucket", "bucket");
    }

    @AfterEach
    void clear() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void 트랜잭션_안에서는_커밋된_뒤에_지운다() {
        TransactionSynchronizationManager.initSynchronization();
        component.deleteFileAfterCommit(URL);
        verify(s3, never()).deleteObject(any(DeleteObjectRequest.class));

        TransactionSynchronizationManager.getSynchronizations().forEach(TransactionSynchronization::afterCommit);
        verify(s3).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void 롤백되면_지우지_않는다() {
        TransactionSynchronizationManager.initSynchronization();
        component.deleteFileAfterCommit(URL);
        TransactionSynchronizationManager.getSynchronizations()
                .forEach(s -> s.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK));
        verify(s3, never()).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void 이미지가_없으면_아무것도_하지_않는다() {
        component.deleteFileAfterCommit(null);
        verifyNoInteractions(s3);
    }
}
