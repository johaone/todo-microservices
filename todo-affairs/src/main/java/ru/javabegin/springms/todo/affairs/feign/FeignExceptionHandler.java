package ru.javabegin.springms.todo.affairs.feign;

import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

@Component // Spring автоматически добавит в контейнер и будет применять этот класс при вызове UserFeignClient
public class FeignExceptionHandler implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 406:
                return new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, readMassage(response));
        }

        return null;

    }

    // получить сообщение ошибки из потока в формате String

    private String readMassage(Response response) {

        String massage = null;
        Reader reader = null;

        try {

            // из Stream делаем String
            reader = response.body().asReader(Charset.defaultCharset());
            massage = CharStreams.toString(reader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return massage;

    }
}
