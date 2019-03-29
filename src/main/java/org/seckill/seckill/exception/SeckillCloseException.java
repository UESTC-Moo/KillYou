package org.seckill.seckill.exception;

import org.seckill.seckill.dto.SeckillExecution;

public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
