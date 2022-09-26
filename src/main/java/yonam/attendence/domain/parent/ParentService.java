package yonam.attendence.domain.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yonam.attendence.web.parent.ParentLoginForm;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    public Parent login(ParentLoginForm form) {

        Parent parent = parentRepository.findByNamePhone(form.getName(), form.getPhone());

        if (parent == null) {
            return null; //부모님 폰과 번호에 매칭 없음
        }

        return parent;
    }
}
