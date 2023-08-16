package com.samsung.cs.batch.reader;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.item.data.RepositoryItemReader;

import com.samsung.cs.model.entity.key.Ztinv001Id;
import com.samsung.cs.repository.Ztinv001Repository;

public class Ztinv001Reader<Ztinv001> extends RepositoryItemReader<Ztinv001> {
		
	
	public Ztinv001Reader(Ztinv001Repository ztinv001Repository) {
			super();
			setRepository(ztinv001Repository);
			setMethodName("getEntitiesByCompositeKeys");
			setArguments(Collections.singletonList(null));
	}
	
	
	public void setCompositeKeys(List<Ztinv001Id> compositeKeys) {
		setArguments(Collections.singletonList(compositeKeys));
	}


}
