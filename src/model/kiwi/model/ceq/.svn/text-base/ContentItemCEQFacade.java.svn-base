package kiwi.model.ceq;


import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;


/**
 * Provide access to the CEQ information for a content item.<br/>
 * In this interface are uses the next acronyms :
 * <ul>
 * <li>IQ - Information eQuity
 * <li>PQ - Participation eQuity
 * <li>CEQ - Community EQuity
 * <li>PQ - Participation eQuity
 * <li>TQ - Tags eQuity
 * <li>CQ - Contribution eQuity
 * <li>K and B suffixes means K and B parameters of equity aging
 * function. The reason for this is that the IQ is a dynamic
 * value, it is changed every second (aging), so at least two
 * parameter are needed to get immediate value, in a very
 * simplistic way the IQ is calculated like this :
 * 
 * <pre>
 * IQ = IQK * T + IQB
 * </pre>
 * 
 * </ul>
 * If the acronyms are used in the methods names then the name
 * was altered according with the hungarian notation.
 *
 * @deprecated the equity values are now intern calculated,
 *             consider the
 *             kiwi.service.equity.EquityServiceImpl.
 * @author mradules
 * @version 07-pre
 * @since 07-pre
 */
@KiWiFacade
@RDFType( {Constants.NS_KIWI_CORE + "ContentItem"})
@Deprecated
public interface ContentItemCEQFacade extends ContentItemI {

    /**
     * Returns the Information Equity value. The information equity captures the social activities
     * around an information and dynamically calculates a numeric value that represents the
     * importance, relevance and quality of the information. The activities captured includes:
     * <ul>
     * <li>how many times users have viewed the information</li>
     * <li>how many times this information has been downloaded by users</li>
     * <li>how many times this information was reused by an individual</li>
     * <li>user feedback (ratings, comments, ...)</li>
     * </ul>
     *
     * @return the Information Equity value.
     */
    @RDF(Constants.NS_KIWI_CEQ + "iq")
    public double getInformationEquity();

    /**
     * Sets a new value for the information equity.
     *
     * @param iq the new value for the information equity.
     */
    public void setInformationEquity(double iq);

    @RDF(Constants.NS_KIWI_CEQ + "iqk")
    public int getIQK();

    // I am not shore if I need this property
    public void setIQK(int i);

    @RDF(Constants.NS_KIWI_CEQ + "iqb")
    public int getIQB();

    // I am not shore if I need this property
    public void setIQB(int i);

    /**
     * Returns the Participation Equity for this content item.
     *
     * @return the Participation Equity for this content item.
     */
    @RDF(Constants.NS_KIWI_CEQ + "pq")
    public double getParticipationEquity();

    /**
     * Registers a new value for Participation Equity for this
     * content item.
     *
     * @return the new Participation Equity value for this
     *         content item.
     */
    public void setParticipationEquity(double pq);

    @RDF(Constants.NS_KIWI_CEQ + "pqk")
    public int getPQK();

    public void setPQK(int i);

    @RDF(Constants.NS_KIWI_CEQ + "pqb")
    public int getPQB();

    public void setPQB(int i);
}
