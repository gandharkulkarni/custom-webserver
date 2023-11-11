package hotelapp;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Threadsafe hashmap builder class extends HashMapBuilder
 * By: Gandhar Kulkarni
 */
public class ThreadSafeHashMapBuilder extends HashMapBuilder {
    private ReentrantReadWriteLock lock;

    /**
     * Constructor for threadsafe hashmap builder class
     */
    public ThreadSafeHashMapBuilder() {
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Implements updateHotelHashMap using locks
     * @param hotels List of Hotel objects
     */
    @Override
    public void updateHotelHashMap(List<Hotel> hotels) {
        try {
            lock.writeLock().lock();
            super.updateHotelHashMap(hotels);
        }
        catch (Exception ex){
            LogHelper.getLogger().error(ex);
        }
        finally {
            lock.writeLock().unlock();

        }
    }

    /**
     * Implements updateHotelReviewHashMap using locks
     * @param reviews List of Review objects
     */
    @Override
    public void updateHotelReviewHashMap(List<Review> reviews){
        try {
            lock.writeLock().lock();
            super.updateHotelReviewHashMap(reviews);
        }
        catch (Exception ex){
            LogHelper.getLogger().error(ex);
        }
        finally {
            lock.writeLock().unlock();

        }
    }

    /**
     * Implements updateWordHashMap using locks
     * @param reviews List of Review objects
     */
    @Override
    public void updateWordHashMap(List<Review> reviews){
        try {
            lock.writeLock().lock();
            super.updateWordHashMap(reviews);
        }
        catch (Exception ex){
            LogHelper.getLogger().error(ex);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

}
