package com.epam.jwd.Conferences.dto;

import java.io.Serializable;

public interface DatabaseEntity <I extends Serializable> {
    I getId();
}
