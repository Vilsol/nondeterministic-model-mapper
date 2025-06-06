package demo;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelMapperTest {

    @Test
    public void testFlaky() {
        ModelMapper modelMapper = new ModelMapper();

        AtomicBoolean gotCalled = new AtomicBoolean(false);
        modelMapper.createTypeMap(LocalDateTime.class, Timestamp.class).setConverter(context -> {
            System.out.println("Got called!");
            gotCalled.set(true);
            return Timestamp.newBuilder()
                    .setSeconds(context.getSource().toEpochSecond(ZoneOffset.UTC))
                    .setNanos(context.getSource().getNano())
                    .build();
        });

        DomainSample sample = new DomainSample();
        sample.setHello("foo");
        sample.setWorld(LocalDateTime.ofEpochSecond(123, 456, ZoneOffset.UTC));

        Sample out = modelMapper.map(sample, Sample.Builder.class).build();

        if (!gotCalled.get()) {
            System.out.println("Did not call!");
        }

        assert gotCalled.get();
        assert out.getHello().equals("foo");
        assert out.getWorld().equals(Timestamp.newBuilder().setSeconds(123).setNanos(456).build());
    }

}
