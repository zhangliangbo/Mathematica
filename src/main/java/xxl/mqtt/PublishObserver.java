package xxl.mqtt;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PublishObserver implements Observer<IMqttDeliveryToken> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            iMqttDeliveryToken.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
